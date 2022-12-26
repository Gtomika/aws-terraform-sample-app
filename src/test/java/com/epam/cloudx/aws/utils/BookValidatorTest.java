package com.epam.cloudx.aws.utils;

import com.epam.cloudx.aws.domain.Book;
import com.epam.cloudx.aws.exceptions.BookValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookValidatorTest {

    private static final String TEST_ISBN = "1-84356-028-3";

    private BookValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new BookValidator(1800);
    }

    @Test
    public void shouldAcceptValidBookCreateRequest() {
        Book book = testBook();
        assertDoesNotThrow(() -> validator.validateBookCreateRequest(book));
    }

    @Test
    public void shouldNotAcceptInvalidBookCreateRequest() {
        Book book = testBook();
        book.setAuthor(null);
        book.setPublishYear(1700);
        BookValidationException e = assertThrows(BookValidationException.class,
                () -> validator.validateBookCreateRequest(book));
        assertEquals(2, e.getErrorContext().size());
    }

    @Test
    public void shouldAcceptValidBookUpdateRequest() {
        Book book = testBook();
        assertDoesNotThrow(() -> validator.validateBookUpdateRequest(testBook().getIsbn(), book));
    }

    @Test
    public void shouldNotAcceptInvalidBookUpdateRequest() {
        Book book = testBook();
        BookValidationException e = assertThrows(BookValidationException.class,
                () -> validator.validateBookUpdateRequest("85-359-0277-5", book));
        assertEquals(1, e.getErrorContext().size());
    }

    private Book testBook() {
        return Book.builder()
                .isbn(TEST_ISBN)
                .title("Some book")
                .author("Some author")
                .publishYear(2002)
                .build();
    }

}