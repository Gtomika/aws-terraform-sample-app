package com.epam.cloudx.aws.controller;

import com.epam.cloudx.aws.IntegrationTestInitializer;
import com.epam.cloudx.aws.domain.Book;
import com.epam.cloudx.aws.repositories.BookRepository;
import com.epam.cloudx.aws.utils.BookTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"integrationTest"})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerIntegrationTest extends IntegrationTestInitializer {

    private static final String APPLICATION_JSON = "application/json";
    private static final Book TEST_BOOK = BookTestUtils.testBook();
    private static final String BOOK_URL = "/api/vi/books";
    private static final String BOOK_ISBN_URL = BOOK_URL + "/{isbn}";
    private static final String BOOK_IMAGE_URL = BOOK_ISBN_URL + "/image";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    @Order(1)
    public void shouldCreateBook() throws Exception {
        mockMvc.perform(post(BOOK_URL)
                .accept(APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(TEST_BOOK)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isbn").value(TEST_BOOK.getIsbn()));
    }

    @Test
    @Order(2)
    public void shouldGetBook() throws Exception {
        mockMvc.perform(get(BOOK_ISBN_URL, TEST_BOOK.getIsbn()).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(TEST_BOOK.getIsbn()))
                .andExpect(jsonPath("$.title").value(TEST_BOOK.getTitle()))
                .andExpect(jsonPath("$.imageUrl").doesNotExist());
    }

}
