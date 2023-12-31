package com.gaspar.cloudx.aws.services;

import static com.gaspar.cloudx.aws.utils.BookTestUtils.testBook;
import static org.junit.jupiter.api.Assertions.*;

import com.gaspar.cloudx.aws.domain.Book;
import com.gaspar.cloudx.aws.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookValidatorServiceTest {

    private BookValidatorService validator;

    @BeforeEach
    public void setUp() {
        validator = new BookValidatorService();
        validator.setMinPublishYear(1800);
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
        ValidationException e = assertThrows(ValidationException.class,
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
        ValidationException e = assertThrows(ValidationException.class,
                () -> validator.validateBookUpdateRequest("85-359-0277-5", book));
        assertEquals(1, e.getErrorContext().size());
    }



}