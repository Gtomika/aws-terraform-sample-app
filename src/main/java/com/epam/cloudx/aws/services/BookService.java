package com.epam.cloudx.aws.services;

import com.epam.cloudx.aws.domain.Book;
import com.epam.cloudx.aws.exceptions.BookApiException;
import com.epam.cloudx.aws.exceptions.BookDuplicationException;
import com.epam.cloudx.aws.exceptions.BookNotFoundException;
import com.epam.cloudx.aws.repositories.BookRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookValidatorService bookValidatorService;
    private final BookImagesService bookImagesService;

    public Book getBook(String isbn) {
        return bookRepository.getBook(isbn);
    }

    public Book createBook(Book book) {
        bookValidatorService.validateBookCreateRequest(book);
        if(!bookRepository.existsByIsbn(book.getIsbn())) {
            log.info("New book created: {}", book);
            return bookRepository.createOrUpdateBook(book);
        } else {
            throw new BookDuplicationException(book.getIsbn());
        }
    }

    public Book updateBook(String isbn, Book book) {
        bookValidatorService.validateBookUpdateRequest(isbn, book);
        if(bookRepository.existsByIsbn(book.getIsbn())) {
            return bookRepository.createOrUpdateBook(book);
        } else {
            throw new BookNotFoundException(isbn);
        }
    }

    /**
     * Validate and upload a book cover image. If successful, it will
     * set the 'imagePath' field of the book entity to the new S3 key.
     */
    public Book updateBookImage(String isbn, MultipartFile image) {
        Book book = getBook(isbn);
        String s3Key;
        try {
            //check contents
            bookImagesService.validateImage(image);
            //copy to temporary file
            Path tempImage = Files.createTempFile("book_cover_image", "");
            image.transferTo(tempImage);
            //upload to s3
            s3Key = bookImagesService.uploadImage(isbn, tempImage, image.getContentType(), image.getSize());
            Files.delete(tempImage);
        } catch (IOException e) {
            throw new BookApiException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //upload success, set field in entity
        book.setImagePath(s3Key);
        return bookRepository.createOrUpdateBook(book);
    }

    public void deleteBook(String isbn) {
        Book book = getBook(isbn);
        if(book.getImagePath() != null) {
            bookImagesService.deleteImage(book.getImagePath());
        }
        bookRepository.deleteBook(isbn);
        log.info("Deleted book: {}", book);
    }

}
