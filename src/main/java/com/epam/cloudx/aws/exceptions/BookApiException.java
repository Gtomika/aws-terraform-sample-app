package com.epam.cloudx.aws.exceptions;

import lombok.Getter;

import java.util.List;

public class BookApiException extends RuntimeException {

    @Getter
    private final String errorCode;

    /**
     * Optionally, additional data about the exception.
     */
    @Getter
    private final List<String> errorContext;

    public BookApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.errorContext = null;
    }

    public BookApiException(String message, String errorCode, List<String> errorContext) {
        super(message);
        this.errorCode = errorCode;
        this.errorContext = errorContext;
    }
}
