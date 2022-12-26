package com.epam.cloudx.aws.services;

import com.epam.cloudx.aws.domain.Book;
import com.epam.cloudx.aws.exceptions.BookDuplicationException;
import com.epam.cloudx.aws.exceptions.BookNotFoundException;
import com.epam.cloudx.aws.exceptions.BookValidationException;
import com.epam.cloudx.aws.repositories.BookRepository;
import com.epam.cloudx.aws.utils.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookValidator bookValidator;

    public List<Book> getBooks() {
        //TODO it would be probably better to paginate this response
        return null; //TODO
    }

    public Book getBookByIsbn(String isbn) throws BookNotFoundException {
        return null; //TODO
    }

    public Book createBook(Book book) throws BookDuplicationException, BookValidationException {
        bookValidator.validateBookCreateRequest(book);
        return null; //TODO
    }

    public Book updateBook(String isbn, Book book) throws BookNotFoundException, BookValidationException {
        bookValidator.validateBookUpdateRequest(isbn, book);
        return null; //TODO
    }

    public void deleteBook(String isbn) throws BookNotFoundException {
        //TODO
    }

}
