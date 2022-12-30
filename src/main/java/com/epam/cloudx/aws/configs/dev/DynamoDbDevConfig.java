package com.epam.cloudx.aws.configs.dev;

import com.epam.cloudx.aws.domain.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Slf4j
@Configuration
@Profile("dev | integrationTest")
public class DynamoDbDevConfig {

    @Value("${infrastructure.aws-region}")
    private String awsRegion;

    @Value("${infrastructure.book-data-table.name}")
    private String bookTableName;

    @Value("${infrastructure.dynamodb-localstack-endpoint}")
    private String dynamodbLocalstackEndpoint;

    /**
     * For the dev client we must use the localstack endpoint.
     */
    @Bean
    public DynamoDbEnhancedClient dynamoDbClientDev() {
        log.debug("Connecting to localstack DynamoDB on '{}'", dynamodbLocalstackEndpoint);
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of(awsRegion))
                .endpointOverride(URI.create(dynamodbLocalstackEndpoint))
                .build();
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTable<Book> bookTableDev() {
        return dynamoDbClientDev().table(bookTableName, TableSchema.fromBean(Book.class));
    }

}
