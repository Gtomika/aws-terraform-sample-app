package com.epam.cloudx.aws.exceptions;

import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BookApiException extends RuntimeException {

    @Getter
    private final HttpStatus errorCode;

    /**
     * Optionally, additional data about the exception.
     */
    @Getter
    private final List<String> errorContext;

    public BookApiException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.errorContext = null;
    }

    public BookApiException(String message, HttpStatus errorCode, List<String> errorContext) {
        super(message);
        this.errorCode = errorCode;
        this.errorContext = errorContext;
    }
}
