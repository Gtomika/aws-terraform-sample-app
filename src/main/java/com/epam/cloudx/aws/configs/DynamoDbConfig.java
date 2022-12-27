package com.epam.cloudx.aws.configs;

import com.epam.cloudx.aws.domain.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
public class DynamoDbConfig {

    @Value("${infrastructure.book-data-table.name}")
    private String bookTableName;

    @Bean
    public DynamoDbEnhancedClient dynamoDbClient() {
        return DynamoDbEnhancedClient.create();
    }

    @Bean
    public DynamoDbTable<Book> bookTable() {
        return dynamoDbClient().table(bookTableName, TableSchema.fromBean(Book.class));
    }

}
