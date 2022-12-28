package com.epam.cloudx.aws.services;

import com.epam.cloudx.aws.domain.Book;
import com.epam.cloudx.aws.exceptions.BookValidationException;
import com.epam.cloudx.aws.services.BookValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.epam.cloudx.aws.utils.BookTestUtils.testBook;
import static org.junit.jupiter.api.Assertions.*;

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



}