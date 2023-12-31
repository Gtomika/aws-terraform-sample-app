package com.gaspar.cloudx.aws.utils;

import com.gaspar.cloudx.aws.domain.Book;

public class BookTestUtils {

    public static final String TEST_ISBN = "1-84356-028-3";

    public static Book testBook() {
        return Book.builder()
                .isbn(TEST_ISBN)
                .title("Some book")
                .author("Some author")
                .publishYear(2002)
                .build();
    }

}
