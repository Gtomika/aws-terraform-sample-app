package com.epam.cloudx.aws.services;

import com.epam.cloudx.aws.domain.Book;
import com.epam.cloudx.aws.exceptions.BookDuplicationException;
import com.epam.cloudx.aws.exceptions.BookNotFoundException;
import com.epam.cloudx.aws.repositories.BookRepository;
import com.epam.cloudx.aws.utils.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookValidator bookValidator;

    public Book getBook(String isbn) {
        return bookRepository.getBook(isbn);
    }

    public Book createBook(Book book) {
        bookValidator.validateBookCreateRequest(book);
        if(!bookRepository.existsByIsbn(book.getIsbn())) {
            return bookRepository.createOrUpdateBook(book);
        } else {
            throw new BookDuplicationException(book.getIsbn());
        }
    }

    public Book updateBook(String isbn, Book book) {
        bookValidator.validateBookUpdateRequest(isbn, book);
        if(bookRepository.existsByIsbn(book.getIsbn())) {
            return bookRepository.createOrUpdateBook(book);
        } else {
            throw new BookNotFoundException(isbn);
        }
    }

    public void deleteBook(String isbn) {
        bookRepository.deleteBook(isbn);
    }

}
