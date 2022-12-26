package com.epam.cloudx.aws.exceptions;

import java.util.List;

public class BookValidationException extends BookApiException {

    private static final String ERROR_CODE = "BOOK_VALIDATION_FAILED";

    public BookValidationException(String message, List<String> errorContext) {
        super(message, ERROR_CODE, errorContext);
    }

}
