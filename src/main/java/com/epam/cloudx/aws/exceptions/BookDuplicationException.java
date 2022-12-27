package com.epam.cloudx.aws.exceptions;


public class BookDuplicationException extends BookApiException {

    private static final String ERROR_CODE = "BOOK_DUPLICATION";

    public BookDuplicationException(String isbn) {
        super(String.format("Book already exists with ISBN '%s'", isbn), ERROR_CODE);
    }

}
