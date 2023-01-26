package com.epam.cloudx.aws;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SNS;
import static org.testcontainers.utility.DockerImageName.parse;
import static org.testcontainers.utility.ImageNameSubstitutor.instance;

import java.io.IOException;
import java.time.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;


/**
 * Initializes the same docker compose setup that is
 * used for the local environment. Integration test classes
 * should extend this class.
 */
public class IntegrationTestInitializer {

    private static final String MOCK_BUCKET_NAME = "epam-cloudx-book-cover-images";
    private static final String MOCK_TABLE_NAME = "EpamCloudxBookData";
    private static final String MOCK_CUSTOMER_TABLE_NAME = "EpamCloudxCustomerData";
    private static final String MOCK_TOPIC_NAME = "epam-cloudx-book-notifications";

    public static LocalStackContainer localStackContainer = new LocalStackContainer(instance()
            .apply(parse("localstack/localstack:1.3.1"))
            .asCompatibleSubstituteFor("localstack/localstack"))
            .withServices(S3, DYNAMODB, SNS)
            .withReuse(false);

    public static GenericContainer<?> memcachedContainer = new GenericContainer<>(instance()
            .apply(parse("memcached"))
            .asCompatibleSubstituteFor("memcached"))
            .withExposedPorts(11211);

    @BeforeAll
    public static void startTestContainers() {
        memcachedContainer.start();

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
            localStackContainer.execInContainer("awslocal", "dynamodb", "create-table",
                    "--table-name", MOCK_CUSTOMER_TABLE_NAME,
                    "--attribute-definitions", "AttributeName=id,AttributeType=S",
                    "--key-schema", "AttributeName=id,KeyType=HASH",
                    "--provisioned-throughput", "ReadCapacityUnits=5,WriteCapacityUnits=5");
            localStackContainer.execInContainer("awslocal", "sns", "create-topic", "--name", MOCK_TOPIC_NAME);
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
        registry.add("infrastructure.s3-localstack-endpoint", () -> localStackContainer.getEndpointOverride(S3));
        registry.add("infrastructure.dynamodb-localstack-endpoint", () -> localStackContainer.getEndpointOverride(DYNAMODB));
        registry.add("infrastructure.sns-localstack-endpoint", () -> localStackContainer.getEndpointOverride(SNS));

        registry.add("infrastructure.book-data-table.name", () -> MOCK_TABLE_NAME);
        registry.add("infrastructure.customer-data-table.name", () -> MOCK_CUSTOMER_TABLE_NAME);

        registry.add("infrastructure.book-images-bucket.url", () -> String.format(
                "http://localhost:%d/s3/%s", localstackExposedPort, MOCK_BUCKET_NAME
        ));
        registry.add("infrastructure.book-images-bucket.name", () -> MOCK_BUCKET_NAME);
        registry.add("logging.level.com.epam.cloudx.aws", () -> "DEBUG");

        registry.add("infrastructure.elasticache-cluster.url", () -> memcachedContainer.getHost());
        registry.add("infrastructure.elasticache-cluster.port", () -> memcachedContainer.getFirstMappedPort());
        registry.add("infrastructure.elasticache-cluster.time-to-live", () -> 60);

        registry.add("infrastructure.book-notifications-topic.arn", () -> String.format(
                "arn:aws:sns:%s:%s:%s", localStackContainer.getRegion(), "000000000000", MOCK_TOPIC_NAME
        ));
    }

}