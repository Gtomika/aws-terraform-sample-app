package com.gaspar.cloudx.aws.controllers.advice;

import com.gaspar.cloudx.aws.controllers.dto.BookApiErrorResponse;
import com.gaspar.cloudx.aws.exceptions.BookApiException;
import com.gaspar.cloudx.aws.mappers.ExceptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class BookApiControllerAdvice {

    private final ExceptionMapper exceptionMapper;

    @ExceptionHandler(BookApiException.class)
    public ResponseEntity<BookApiErrorResponse> handleBookApiException(BookApiException e) {
        var errorResponse = exceptionMapper.mapException(e);
        return new ResponseEntity<>(errorResponse, errorResponse.getErrorCode());
    }

}
