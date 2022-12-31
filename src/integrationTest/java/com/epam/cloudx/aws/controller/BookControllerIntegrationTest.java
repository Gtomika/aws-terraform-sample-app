package com.epam.cloudx.aws.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.cloudx.aws.IntegrationTestInitializer;
import com.epam.cloudx.aws.domain.Book;
import com.epam.cloudx.aws.repositories.BookRepository;
import com.epam.cloudx.aws.utils.BookTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Performs a series of operations on the Book API: create, update
 * and delete a book. The test ordering is important for this test class.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"integrationTest"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerIntegrationTest extends IntegrationTestInitializer {

    private static final Book TEST_BOOK = BookTestUtils.testBook();
    private static final String NOT_EXISTING_ISBN = "12345";

    private static final String BOOK_URL = "/api/v1/books";
    private static final String BOOK_ISBN_URL = BOOK_URL + "/{isbn}";
    private static final String BOOK_IMAGE_URL = BOOK_ISBN_URL + "/image";

    @Value("${infrastructure.book-images-bucket.url}")
    private String bucketUrl;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    @Order(1)
    public void shouldCreateBook() throws Exception {
        mockMvc.perform(post(BOOK_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(TEST_BOOK)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value(TEST_BOOK.getIsbn()));
    }

    @Test
    @Order(2)
    public void shouldGetBook() throws Exception {
        mockMvc.perform(get(BOOK_ISBN_URL, TEST_BOOK.getIsbn()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(TEST_BOOK.getIsbn()))
                .andExpect(jsonPath("$.title").value(TEST_BOOK.getTitle()))
                .andExpect(jsonPath("$.imageUrl").doesNotExist());
    }

    @Test
    @Order(3)
    public void shouldNotFindNotExistingBook() throws Exception {
        mockMvc.perform(get(BOOK_ISBN_URL, NOT_EXISTING_ISBN).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(4)
    public void shouldUpdateBook() throws Exception {
        Book updatedBook = BookTestUtils.testBook();
        updatedBook.setTitle("Something else");
        mockMvc.perform(put(BOOK_ISBN_URL, TEST_BOOK.getIsbn())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(TEST_BOOK.getIsbn()))
                .andExpect(jsonPath("$.title").value(updatedBook.getTitle()));
    }

    @Test
    @Order(5)
    public void shouldNotUpdateNotExistingBook() throws Exception {
        Book updatedBook = BookTestUtils.testBook();
        updatedBook.setIsbn(NOT_EXISTING_ISBN);
        mockMvc.perform(put(BOOK_ISBN_URL, NOT_EXISTING_ISBN)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(6)
    public void shouldNotUpdateBookIsbn() throws Exception {
        Book updatedBook = BookTestUtils.testBook();
        updatedBook.setIsbn(NOT_EXISTING_ISBN);
        mockMvc.perform(put(BOOK_ISBN_URL, TEST_BOOK.getIsbn())
                 .accept(MediaType.APPLICATION_JSON)
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(jsonMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    @Disabled("Localstack produces 500 internal error on upload. See README.")
    public void shouldUpdateBookImage() throws Exception {
        mockMvc.perform(multipart(BOOK_IMAGE_URL, TEST_BOOK.getIsbn())
                .file(getTestImage())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(TEST_BOOK.getIsbn()))
                .andExpect(jsonPath("$.imageUrl").value(bucketUrl + "/" + TEST_BOOK.getIsbn() + ".jpeg"));
    }

    @Test
    @Order(7) //TODO remove this when localstack S3 issue is resolved
    public void shouldReturnAbsoluteLogoUrl() throws Exception {
        //manually setting logo path for the entity
        Book book = BookTestUtils.testBook();
        book.setImagePath(book.getIsbn() + ".jpg");
        bookRepository.createOrUpdateBook(book);

        mockMvc.perform(get(BOOK_ISBN_URL, book.getIsbn())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(book.getIsbn()))
                .andExpect(jsonPath("$.imageUrl").value(bucketUrl + "/" + book.getIsbn() + ".jpg"));

        bookRepository.createOrUpdateBook(TEST_BOOK);
    }

    @Test
    @Order(8)
    public void shouldNotUpdateNotExistingBookImage() throws Exception {
        mockMvc.perform(multipart(BOOK_IMAGE_URL, NOT_EXISTING_ISBN)
                .file(getTestImage())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    public void shouldNotUpdateInvalidBookImage() throws Exception {
        var invalidImage = new MockMultipartFile("image", "book.jpg", "random-content-type", new byte[]{1,2});
        mockMvc.perform(multipart(BOOK_IMAGE_URL, TEST_BOOK.getIsbn())
                .file(invalidImage)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(10)
    public void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete(BOOK_ISBN_URL, TEST_BOOK.getIsbn())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldNotDeleteNotExistingBook() throws Exception {
        mockMvc.perform(delete(BOOK_ISBN_URL, NOT_EXISTING_ISBN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private MockMultipartFile getTestImage() throws IOException {
        var bytes = Files.readAllBytes(new ClassPathResource("book.jpg").getFile().toPath());
        return new MockMultipartFile("image", "book.jpg", "image/jpeg", bytes);
    }

}
