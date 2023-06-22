package com.gaspar.cloudx.aws.mappers;

import static org.junit.jupiter.api.Assertions.*;

import com.gaspar.cloudx.aws.controllers.dto.BookRequest;
import com.gaspar.cloudx.aws.controllers.dto.BookResponse;
import com.gaspar.cloudx.aws.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MapperTestConfig.class)
class BookMapperTest {

    @Value("${infrastructure.book-images-bucket.url}")
    private String bookImagesBucketUrl;

    @Autowired
    private BookMapper bookMapper;

    @Test
    public void shouldMapToBookFromRequest() {
        BookRequest request = new BookRequest();
        request.setIsbn("123");
        request.setTitle("Some book");
        request.setAuthor("Some author");

        Book book = bookMapper.mapToBook(request);

        assertEquals(request.getIsbn(), book.getIsbn());
        assertEquals(request.getTitle(), book.getTitle());
        assertEquals(request.getAuthor(), book.getAuthor());
        assertEquals(request.getPublishYear(), book.getPublishYear());
        assertNull(book.getImagePath());
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc.jpg"})
    @NullSource
    public void shouldMapToBookResponseFromBook(String imagePath) {
        Book book = Book.builder()
                .isbn("123")
                .title("Title")
                .author("Author")
                .imagePath(imagePath)
                .publishYear(1900)
                .build();

        BookResponse response = bookMapper.mapToBookResponse(book);

        assertEquals(book.getIsbn(), response.getIsbn());
        assertEquals(book.getTitle(), response.getTitle());
        assertEquals(book.getAuthor(), response.getAuthor());
        assertEquals(book.getPublishYear(), response.getPublishYear());
        if(imagePath == null) {
            assertNull(response.getImageUrl());
        } else {
            assertEquals(bookImagesBucketUrl + "/" + book.getImagePath(), response.getImageUrl());
        }
    }

}