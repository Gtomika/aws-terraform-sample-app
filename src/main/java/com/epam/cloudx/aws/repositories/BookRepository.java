package com.epam.cloudx.aws.repositories;

import com.epam.cloudx.aws.domain.Book;
import com.epam.cloudx.aws.exceptions.BookApiException;
import com.epam.cloudx.aws.exceptions.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BookRepository {

    private final DynamoDbTable<Book> bookTable;

    public boolean existsByIsbn(String isbn) {
        try {
            Book book = bookTable.getItem(keyFromIsbn(isbn));
            return book != null;
        } catch (SdkException e) {
            logAndThrow("Error while checking book existence", e);
            return false;
        }
    }

    public Book getBook(String isbn) {
        try {
            Book book = bookTable.getItem(keyFromIsbn(isbn));
            if(book == null) {
                throw new BookNotFoundException(isbn);
            }
            return book;
        } catch (SdkException e) {
            logAndThrow("Error while fetching book", e);
            return null;
        }
    }

    public Book createOrUpdateBook(Book book) {
        try {
            bookTable.putItem(book);
            return book;
        } catch (SdkException e) {
            logAndThrow("Error create/update of book", e);
            return null;
        }
    }

    public void deleteBook(String isbn) {
        try {
            bookTable.deleteItem(keyFromIsbn(isbn));
        } catch (SdkException e) {
            logAndThrow("Error while deleting book", e);
        }
    }

    private Key keyFromIsbn(String isbn) {
        return Key.builder()
                .partitionValue(isbn)
                .build();
    }

    private void logAndThrow(String message, SdkException e) {
        log.error(message, e);
        throw new BookApiException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
