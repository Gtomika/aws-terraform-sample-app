package com.epam.cloudx.aws.controllers.advice;

import com.epam.cloudx.aws.controllers.BookController;
import com.epam.cloudx.aws.domain.BookApiError;
import com.epam.cloudx.aws.exceptions.BookDuplicationException;
import com.epam.cloudx.aws.exceptions.BookNotFoundException;
import com.epam.cloudx.aws.exceptions.BookValidationException;
import com.epam.cloudx.aws.mappers.ExceptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = BookController.class)
public class BookControllerAdvice {

    private final ExceptionMapper exceptionMapper;

    @ExceptionHandler(BookDuplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BookApiError handleBookDuplication(BookDuplicationException e) {
        return exceptionMapper.mapException(e);
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BookApiError handleBookNotFound(BookNotFoundException e) {
        return exceptionMapper.mapException(e);
    }

    @ExceptionHandler(BookValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BookApiError handleBookValidation(BookValidationException e) {
        return exceptionMapper.mapException(e);
    }

}
