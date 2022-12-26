package com.epam.cloudx.aws.utils;

import com.epam.cloudx.aws.domain.Book;
import com.epam.cloudx.aws.exceptions.BookValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Validates requests of the Book API. This may be
 * done also with the Spring validation somehow.
 */
@Component
public class BookValidator {

    private final int minPublishYear;

    public BookValidator(@Value("${book-api.validation.min-published-year}") int minPublishYear) {
        this.minPublishYear = minPublishYear;
    }

    /**
     * @param book Book that will be created, if validation is successful.
     */
    public void validateBookCreateRequest(Book book) throws BookValidationException {
        var errorContext = new ArrayList<String>();
        commonBookValidations(book, errorContext);
        throwIfInvalid("Validation for book creation has failed", errorContext);
    }

    /**
     * @param isbn ISBN provided in the path of the request.
     * @param book Updated state of the book to be validated.
     */
    public void validateBookUpdateRequest(String isbn, Book book) throws BookValidationException {
        var errorContext = new ArrayList<String>();
        commonBookValidations(book, errorContext);
        if(!StringUtils.equals(isbn, book.getIsbn())) errorContext.add("ISBN of the book cannot be updated");
        throwIfInvalid("Validation for book update has failed", errorContext);
    }

    private void commonBookValidations(Book book, List<String> errorContext) {
        if(StringUtils.isBlank(book.getIsbn())) errorContext.add("ISBN must be provided");
        //TODO: validate if ISBN is in the correct format. I'm not going to do it.
        if(StringUtils.isBlank(book.getTitle())) errorContext.add("Title must be provided");
        if(StringUtils.isBlank(book.getAuthor())) errorContext.add("Author must be provided");
        if(Objects.isNull(book.getPublishYear()) || book.getPublishYear() < minPublishYear) {
            errorContext.add("Book publish year must be provided and be a year after " + minPublishYear);
        }
    }

    private void throwIfInvalid(String message, List<String> errorContext) {
        if(!errorContext.isEmpty()) {
            throw new BookValidationException(message, errorContext);
        }
    }

}