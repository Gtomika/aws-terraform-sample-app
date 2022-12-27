package com.epam.cloudx.aws.controllers.dto;

import lombok.Data;

@Data
public class BookResponse {
    private String isbn;
    private String title;
    private String author;
    /**
     * Absolute URL to the image of this book.
     */
    private String imageUrl;
    private Integer publishYear;
}
