package com.epam.cloudx.aws.exceptions;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends BookApiException {

    public BookNotFoundException(String isbn) {
        super(String.format("Book not found with ISBN '%s'", isbn), HttpStatus.NOT_FOUND);
    }
}
