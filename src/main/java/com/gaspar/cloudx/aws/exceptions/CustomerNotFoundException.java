package com.gaspar.cloudx.aws.exceptions;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends BookApiException {

    public CustomerNotFoundException(String customerId) {
        super(String.format("Customer not found with ID '%s'", customerId), HttpStatus.NOT_FOUND);
    }
}
