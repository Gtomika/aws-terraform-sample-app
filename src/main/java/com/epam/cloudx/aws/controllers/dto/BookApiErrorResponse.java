package com.epam.cloudx.aws.controllers.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class BookApiErrorResponse {
    @JsonIgnore
    private final HttpStatus errorCode;
    private final String message;
    private final List<String> errorContext;
}
