package com.epam.cloudx.aws;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.io.IOException;
import java.time.Duration;

import static org.testcontainers.utility.DockerImageName.parse;
import static org.testcontainers.utility.ImageNameSubstitutor.instance;


/**
 * Initializes the same docker compose setup that is
 * used for the local environment. Integration test classes
 * should extend this class.
 */
public class IntegrationTestInitializer {

    private static final String MOCK_BUCKET_NAME = "epam-cloudx-book-cover-images";
    private static final String MOCK_TABLE_NAME = "EpamCloudxBookData";

    public static LocalStackContainer localStackContainer = new LocalStackContainer(instance()
            .apply(parse("localstack/localstack:1.3.1"))
            .asCompatibleSubstituteFor("localstack/localstack"))
            .withServices(LocalStackContainer.Service.S3, LocalStackContainer.Service.DYNAMODB, LocalStackContainer.Service.SES)
            .withReuse(false);

    @BeforeAll
    public static void startTestContainers() {
        localStackContainer.start();
        localStackContainer.withStartupTimeout(Duration.ofSeconds(5));

        System.setProperty(
                "aws.accessKeyId",
                localStackContainer.getDefaultCredentialsProvider().getCredentials().getAWSAccessKeyId()
        );
        System.setProperty(
                "aws.secretKey",
                localStackContainer.getDefaultCredentialsProvider().getCredentials().getAWSSecretKey()
        );

        try {
            //TODO: find a way to run the shell script from 'local' folder here, and not copy paste commands
            localStackContainer.execInContainer("awslocal", "s3", "mb", "s3://" + MOCK_BUCKET_NAME);
            localStackContainer.execInContainer("awslocal", "dynamodb", "create-table",
                    "--table-name", MOCK_TABLE_NAME,
                    "--attribute-definitions", "AttributeName=isbn,AttributeType=S",
                    "--key-schema", "AttributeName=isbn,KeyType=HASH",
                    "--provisioned-throughput", "ReadCapacityUnits=5,WriteCapacityUnits=5");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Could not initialize localstack container");
        }
    }

    /**
     * Configure properties based on test containers data.
     */
    @DynamicPropertySource
    public static void props(DynamicPropertyRegistry registry) {
        int localstackExposedPort = localStackContainer.getFirstMappedPort();

        registry.add("infrastructure.aws-region", () -> localStackContainer.getRegion());
        registry.add("infrastructure.aws-endpoint", () -> "http://localhost:" + localstackExposedPort);
        registry.add("infrastructure.book-data-table.name", () -> MOCK_TABLE_NAME);
        registry.add("infrastructure.book-images-bucket.url", () -> String.format(
                "http://localhost:%d/s3/%s", localstackExposedPort, MOCK_BUCKET_NAME
        ));
        registry.add("infrastructure.book-images-bucket.name", () -> MOCK_BUCKET_NAME);
        registry.add("logging.level.com.epam.cloudx.aws", () -> "DEBUG");
    }

}