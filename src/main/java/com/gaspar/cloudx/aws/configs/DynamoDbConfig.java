package com.gaspar.cloudx.aws.configs;

import com.gaspar.cloudx.aws.domain.Book;
import com.gaspar.cloudx.aws.domain.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@Profile("prod")
public class DynamoDbConfig {

    @Value("${infrastructure.aws-region}")
    private String awsRegion;

    @Value("${infrastructure.book-data-table.name}")
    private String bookTableName;

    @Value("${infrastructure.customer-data-table.name}")
    private String customerTableName;

    /**
     * <ul>
     *     <li>The SDK will get the credentials from the EC2 instance metadata.</li>
     *     <li>
     *         The route table will direct all calls targeting the DynamoDB public IPs to the DynamoDB gateway endpoint.
     *         DNS resolution must be enabled in the VPC!
     *     </li>
     * </ul>
     */
    @Bean
    public DynamoDbEnhancedClient dynamoDbClient() {
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.of(awsRegion))
                .build();
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTable<Book> bookTable() {
        return dynamoDbClient().table(bookTableName, TableSchema.fromBean(Book.class));
    }

    @Bean
    public DynamoDbTable<Customer> customerTable() {
        return dynamoDbClient().table(customerTableName, TableSchema.fromBean(Customer.class));
    }

}
