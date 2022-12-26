package com.epam.cloudx.aws.domain;

import lombok.Data;

import java.util.List;

@Data
public class BookApiError {
    private final String errorCode;
    private final String message;
    private final List<String> errorContext;
}
