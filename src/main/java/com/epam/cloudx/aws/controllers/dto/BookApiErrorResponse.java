package com.epam.cloudx.aws.controllers.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class BookApiErrorResponse {
    @JsonIgnore
    private final HttpStatus errorCode;
    private final String message;
    private final List<String> errorContext;
}
