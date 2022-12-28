package com.epam.cloudx.aws.controllers.advice;

import com.epam.cloudx.aws.controllers.BookController;
import com.epam.cloudx.aws.controllers.dto.BookApiErrorResponse;
import com.epam.cloudx.aws.exceptions.BookApiException;
import com.epam.cloudx.aws.mappers.ExceptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = BookController.class)
public class BookControllerAdvice {

    private final ExceptionMapper exceptionMapper;

    @ExceptionHandler(BookApiException.class)
    public ResponseEntity<BookApiErrorResponse> handleBookApiException(BookApiException e) {
        var errorResponse = exceptionMapper.mapException(e);
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorCode());
    }

}
