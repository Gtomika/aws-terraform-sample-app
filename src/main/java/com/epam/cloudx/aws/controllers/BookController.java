package com.epam.cloudx.aws.controllers;

import com.epam.cloudx.aws.controllers.dto.BookRequest;
import com.epam.cloudx.aws.controllers.dto.BookResponse;
import com.epam.cloudx.aws.domain.Book;
import com.epam.cloudx.aws.mappers.BookMapper;
import com.epam.cloudx.aws.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping("/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse getBook(@PathVariable String isbn) {
        return bookMapper.mapToBookResponse(bookService.getBook(isbn));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@RequestBody BookRequest bookRequest) {
        Book book = bookMapper.mapToBook(bookRequest);
        return bookMapper.mapToBookResponse(bookService.createBook(book));
    }

    @PutMapping("/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    public BookResponse updateBook(@PathVariable String isbn, @RequestBody BookRequest bookRequest) {
        Book book = bookMapper.mapToBook(bookRequest);
        return bookMapper.mapToBookResponse(bookService.updateBook(isbn, book));
    }

    @DeleteMapping("/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookByIsbn(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
    }

}
