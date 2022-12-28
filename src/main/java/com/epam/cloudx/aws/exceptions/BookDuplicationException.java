package com.epam.cloudx.aws.exceptions;


import org.springframework.http.HttpStatus;

public class BookDuplicationException extends BookApiException {

    public BookDuplicationException(String isbn) {
        super(String.format("Book already exists with ISBN '%s'", isbn), HttpStatus.BAD_REQUEST);
    }
}
