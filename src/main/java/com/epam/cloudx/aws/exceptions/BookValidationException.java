package com.epam.cloudx.aws.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class BookValidationException extends BookApiException {

    public BookValidationException(String message, List<String> errorContext) {
        super(message, HttpStatus.BAD_REQUEST, errorContext);
    }
}
