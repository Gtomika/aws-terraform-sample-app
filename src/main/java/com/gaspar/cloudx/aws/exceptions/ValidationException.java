package com.gaspar.cloudx.aws.exceptions;

import java.util.List;
import org.springframework.http.HttpStatus;

public class ValidationException extends BookApiException {

    public ValidationException(String message, List<String> errorContext) {
        super(message, HttpStatus.BAD_REQUEST, errorContext);
    }
}
