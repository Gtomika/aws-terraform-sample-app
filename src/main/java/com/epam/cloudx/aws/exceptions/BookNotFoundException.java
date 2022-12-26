package com.epam.cloudx.aws.exceptions;

public class BookNotFoundException extends BookApiException {

    private static final String ERROR_CODE = "BOOK_NOT_FOUND";

    public BookNotFoundException(String message) {
        super(message, ERROR_CODE);
    }
}
