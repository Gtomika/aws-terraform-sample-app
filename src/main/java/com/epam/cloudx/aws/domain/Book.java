package com.epam.cloudx.aws.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {

    /**
     * International standard book number,
     * which acts as primary key for the books.
     */
    private String isbn;

    private String title;

    private String author;

    private Integer publishYear;

}
