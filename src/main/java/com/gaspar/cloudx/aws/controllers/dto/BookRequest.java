package com.gaspar.cloudx.aws.controllers.dto;

import lombok.Data;

@Data
public class BookRequest {
    private String isbn;
    private String title;
    private String author;
    private Integer publishYear;
}
