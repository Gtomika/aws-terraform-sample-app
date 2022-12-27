package com.epam.cloudx.aws.controllers.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookApiErrorResponse {
    private final String errorCode;
    private final String message;
    private final List<String> errorContext;
}
