package com.epam.cloudx.aws.repositories;

import com.epam.cloudx.aws.domain.Book;
import com.epam.cloudx.aws.exceptions.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;

@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final DynamoDbTable<Book> bookTable;

    public boolean existsByIsbn(String isbn) {
        try {
            bookTable.getItem(keyFromIsbn(isbn));
            return true;
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    public Book getBook(String isbn) {
        try {
            return bookTable.getItem(keyFromIsbn(isbn));
        } catch (ResourceNotFoundException e) {
            throw new BookNotFoundException(isbn);
        }
    }

    public Book createOrUpdateBook(Book book) {
        bookTable.putItem(book);
        return book;
    }

    public void deleteBook(String isbn) {
        try {
            bookTable.deleteItem(keyFromIsbn(isbn));
        } catch (ResourceNotFoundException e) {
            throw new BookNotFoundException(isbn);
        }
    }

    private Key keyFromIsbn(String isbn) {
        return Key.builder()
                .partitionValue(isbn)
                .build();
    }

}
