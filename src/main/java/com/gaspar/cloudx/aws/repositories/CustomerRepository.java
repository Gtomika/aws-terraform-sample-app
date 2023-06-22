package com.gaspar.cloudx.aws.repositories;

import com.gaspar.cloudx.aws.domain.Customer;
import com.gaspar.cloudx.aws.utils.BookApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {

    private final DynamoDbTable<Customer> customerTable;

    public boolean customerExistsById(String customerId) {
        try {
            Customer customer = customerTable.getItem(keyFromCustomerId(customerId));
            return customer != null;
        } catch (SdkException e) {
            BookApiUtils.logAndThrowInternalError("Failed to fetch customer", e);
            return false;
        }
    }

    public Customer getCustomer(String customerId) {
        try {
            return customerTable.getItem(keyFromCustomerId(customerId));
        } catch (SdkException e) {
            BookApiUtils.logAndThrowInternalError("Failed to fetch customer", e);
            return null;
        }
    }

    public Customer createOrUpdateCustomer(Customer customer) {
        try {
            customerTable.putItem(customer);
            return customer;
        } catch (SdkException e) {
            BookApiUtils.logAndThrowInternalError("Failed to create/update customer", e);
            return null;
        }
    }

    public void deleteCustomer(String customerId) {
        try {
            customerTable.deleteItem(keyFromCustomerId(customerId));
        } catch (SdkException e) {
            BookApiUtils.logAndThrowInternalError("Failed to delete customer", e);
        }
    }

    private Key keyFromCustomerId(String customerId) {
        return Key.builder()
                .partitionValue(customerId)
                .build();
    }
}
