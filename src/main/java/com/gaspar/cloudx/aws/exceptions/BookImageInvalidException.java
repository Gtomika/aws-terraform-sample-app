package com.gaspar.cloudx.aws.exceptions;

import org.springframework.http.HttpStatus;

public class BookImageInvalidException extends BookApiException {

    public BookImageInvalidException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
