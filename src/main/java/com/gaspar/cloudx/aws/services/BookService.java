package com.gaspar.cloudx.aws.services;

import com.gaspar.cloudx.aws.domain.Book;
import com.gaspar.cloudx.aws.domain.CachedResponse;
import com.gaspar.cloudx.aws.exceptions.BookApiException;
import com.gaspar.cloudx.aws.exceptions.BookDuplicationException;
import com.gaspar.cloudx.aws.exceptions.BookNotFoundException;
import com.gaspar.cloudx.aws.repositories.BookCacheRepository;
import com.gaspar.cloudx.aws.repositories.BookRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final BookCacheRepository bookCacheRepository;
    private final BookNotificationsService bookNotificationsService;

    @Setter(onMethod_ = {@Value("${book-api.message-templates.book-added}")})
    private String bookAddedTemplate;

    @Setter(onMethod_ = {@Value("${book-api.message-templates.book-removed}")})
    private String bookRemovedTemplate;

    /**
     * Attempt to get book from the cache first, if that fails
     * then from the database.
     */
    public CachedResponse<Book> getBook(String isbn) {
        var cachedBook = bookCacheRepository.getCachedBook(isbn);
        if(cachedBook.isPresent()) {
            return new CachedResponse<>(cachedBook.get(), true);
        } else {
            log.debug("Book with ISBN '{}' not found in cache, attempting to fetch from DB", isbn);
            Book book = bookRepository.getBook(isbn);
            //cache this book
            bookCacheRepository.cacheBook(book);
            return new CachedResponse<>(book, false);
        }
    }

    public Book createBook(Book book) {
        bookValidatorService.validateBookCreateRequest(book);
        if(!bookRepository.existsByIsbn(book.getIsbn())) {
            Book savedBook = bookRepository.createOrUpdateBook(book);

            //publish email to customers
            bookNotificationsService.sendBookNotification(
                    String.format(bookAddedTemplate, savedBook.getTitle(), savedBook.getAuthor())
            );

            log.info("New book created: {}", savedBook);
            return savedBook;
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
        Book book = bookRepository.getBook(isbn);
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
        Book book = bookRepository.getBook(isbn);
        if(book.getImagePath() != null) {
            bookImagesService.deleteImage(book.getImagePath());
        }
        bookRepository.deleteBook(isbn);
        //also clear from cache
        bookCacheRepository.clearCachedBook(isbn);

        //publish notification about removal
        bookNotificationsService.sendBookNotification(
                String.format(bookRemovedTemplate, book.getTitle(), book.getAuthor())
        );

        log.info("Deleted book: {}", book);
    }

}
