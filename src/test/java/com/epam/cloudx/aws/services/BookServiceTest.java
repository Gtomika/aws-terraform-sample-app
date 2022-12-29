package com.epam.cloudx.aws.services;

import com.epam.cloudx.aws.domain.Book;
import com.epam.cloudx.aws.exceptions.BookDuplicationException;
import com.epam.cloudx.aws.exceptions.BookImageInvalidException;
import com.epam.cloudx.aws.exceptions.BookNotFoundException;
import com.epam.cloudx.aws.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

import static com.epam.cloudx.aws.utils.BookTestUtils.TEST_ISBN;
import static com.epam.cloudx.aws.utils.BookTestUtils.testBook;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookValidatorService bookValidatorService;

    @Mock
    private BookImagesService bookImagesService;

    private BookService bookService;

    @BeforeEach
    public void setUp() {
        bookService = new BookService(bookRepository, bookValidatorService, bookImagesService);
    }

    @Test
    public void shouldGetBook() {
        Book expectedBook = testBook();
        when(bookRepository.getBook(TEST_ISBN)).thenReturn(expectedBook);
        Book resultBook = bookService.getBook(TEST_ISBN);
        assertEquals(expectedBook, resultBook);
    }

    @Test
    public void shouldNotGetNotExistingBook() {
        when(bookRepository.getBook(TEST_ISBN)).thenThrow(BookNotFoundException.class);
        assertThrows(BookNotFoundException.class, () -> bookService.getBook(TEST_ISBN));
    }

    @Test
    public void shouldCreateBook() {
        Book expectedBook = testBook();
        when(bookRepository.createOrUpdateBook(expectedBook)).thenReturn(expectedBook);
        Book resultBook = bookService.createBook(expectedBook);
        assertEquals(expectedBook, resultBook);
    }

    @Test
    public void shouldNotCreateDuplicateBook() {
        Book expectedBook = testBook();
        when(bookRepository.existsByIsbn(expectedBook.getIsbn())).thenReturn(true);
        assertThrows(BookDuplicationException.class, () -> bookService.createBook(expectedBook));
    }

    @Test
    public void shouldUpdateBook() {
        Book expectedBook = testBook();
        when(bookRepository.existsByIsbn(expectedBook.getIsbn())).thenReturn(true);
        when(bookRepository.createOrUpdateBook(expectedBook)).thenReturn(expectedBook);
        Book resultBook = bookService.updateBook(expectedBook.getIsbn(), expectedBook);
        assertEquals(expectedBook, resultBook);
    }

    @Test
    public void shouldNotUpdateNotExistingBook() {
        Book expectedBook = testBook();
        when(bookRepository.existsByIsbn(expectedBook.getIsbn())).thenReturn(false);
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(expectedBook.getIsbn(), expectedBook));
    }

    @Test
    public void shouldUpdateBookImage() {
        Book expectedBook = testBook();
        Book expectedBookWithImagePath = testBook();
        expectedBookWithImagePath.setImagePath(expectedBook.getIsbn() + ".png");

        when(bookRepository.getBook(expectedBook.getIsbn())).thenReturn(expectedBook);
        when(bookImagesService.uploadImage(anyString(), any(Path.class), anyString()))
                .thenReturn(expectedBook.getIsbn() + ".png");
        when(bookRepository.createOrUpdateBook(expectedBookWithImagePath)).thenReturn(expectedBookWithImagePath);

        Book resultBook = bookService.updateBookImage(expectedBook.getIsbn(), mockFile("image/png"));
        assertEquals(expectedBookWithImagePath, resultBook);
    }

    @Test
    public void shouldNotUpdateBookInvalidImage() {
        Book expectedBook = testBook();

        when(bookRepository.getBook(expectedBook.getIsbn())).thenReturn(expectedBook);
        doThrow(BookImageInvalidException.class).when(bookImagesService).validateImage(any(MultipartFile.class));

        assertThrows(BookImageInvalidException.class, () ->
                bookService.updateBookImage(expectedBook.getIsbn(), mockFile("application/json")));
    }

    @Test
    public void shouldNotUpdateNotExistingBookImage() {
        Book expectedBook = testBook();

        when(bookRepository.getBook(expectedBook.getIsbn())).thenThrow(BookNotFoundException.class);

        assertThrows(BookNotFoundException.class, () ->
                bookService.updateBookImage(expectedBook.getIsbn(), mockFile("image/png")));
    }

    @Test
    public void shouldDeleteBook() {
        assertDoesNotThrow(() -> bookService.deleteBook(TEST_ISBN));
    }

    @Test
    public void shouldNotDeleteNotExistingBook() {
        doThrow(BookNotFoundException.class).when(bookRepository).deleteBook(TEST_ISBN);
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(TEST_ISBN));
    }

    private MockMultipartFile mockFile(String contentType) {
        return new MockMultipartFile("image", "image.png", contentType, new byte[]{1,2});
    }
}