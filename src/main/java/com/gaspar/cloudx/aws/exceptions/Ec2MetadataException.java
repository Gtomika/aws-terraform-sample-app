package com.gaspar.cloudx.aws.exceptions;

import org.springframework.http.HttpStatus;

public class Ec2MetadataException extends BookApiException {

    public Ec2MetadataException(String message) {
        super(String.format("Failed to get EC2 metadata: %s", message), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
