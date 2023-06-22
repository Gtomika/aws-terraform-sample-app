package com.gaspar.cloudx.aws.repositories;

import com.gaspar.cloudx.aws.domain.Book;
import com.gaspar.cloudx.aws.exceptions.BookNotFoundException;
import com.gaspar.cloudx.aws.utils.BookApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            BookApiUtils.logAndThrowInternalError("Error while checking book existence", e);
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
            BookApiUtils.logAndThrowInternalError("Error while fetching book", e);
            return null;
        }
    }

    public Book createOrUpdateBook(Book book) {
        try {
            bookTable.putItem(book);
            return book;
        } catch (SdkException e) {
            BookApiUtils.logAndThrowInternalError("Error create/update of book", e);
            return null;
        }
    }

    public void deleteBook(String isbn) {
        try {
            bookTable.deleteItem(keyFromIsbn(isbn));
        } catch (SdkException e) {
            BookApiUtils.logAndThrowInternalError("Error while deleting book", e);
        }
    }

    private Key keyFromIsbn(String isbn) {
        return Key.builder()
                .partitionValue(isbn)
                .build();
    }

}
