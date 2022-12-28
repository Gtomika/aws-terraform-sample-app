package com.epam.cloudx.aws.configs;

import com.epam.cloudx.aws.domain.Book;
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

@Configuration
public class DynamoDbConfig {

    @Value("${infrastructure.aws-region}")
    private String awsRegion;

    @Value("${infrastructure.book-data-table.name}")
    private String bookTableName;

    @Bean
    @Profile("prod")
    public DynamoDbEnhancedClient dynamoDbClient() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of(awsRegion))
                .build();
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    @Profile("prod")
    public DynamoDbTable<Book> bookTable() {
        return dynamoDbClient().table(bookTableName, TableSchema.fromBean(Book.class));
    }

    /**
     * For the dev client we must use the localstack endpoint.
     */
    @Bean
    @Profile("dev | integrationTest")
    public DynamoDbEnhancedClient dynamoDbClientDev() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of(awsRegion))
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    @Profile("dev | integrationTest")
    public DynamoDbTable<Book> bookTableDev() {
        return dynamoDbClientDev().table(bookTableName, TableSchema.fromBean(Book.class));
    }
}
