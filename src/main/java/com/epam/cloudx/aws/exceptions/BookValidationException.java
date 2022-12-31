package com.epam.cloudx.aws.exceptions;

import java.util.List;
import org.springframework.http.HttpStatus;

public class BookValidationException extends BookApiException {

    public BookValidationException(String message, List<String> errorContext) {
        super(message, HttpStatus.BAD_REQUEST, errorContext);
    }
}
