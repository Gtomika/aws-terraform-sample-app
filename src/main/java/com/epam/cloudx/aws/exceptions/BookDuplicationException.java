package com.epam.cloudx.aws.exceptions;


public class BookDuplicationException extends BookApiException {

    private static final String ERROR_CODE = "BOOK_DUPLICATION";

    public BookDuplicationException(String message) {
        super(message, ERROR_CODE);
    }

}
