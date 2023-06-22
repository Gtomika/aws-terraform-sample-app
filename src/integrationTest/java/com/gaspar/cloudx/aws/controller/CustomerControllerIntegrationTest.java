package com.gaspar.cloudx.aws.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gaspar.cloudx.aws.IntegrationTestInitializer;
import com.gaspar.cloudx.aws.controllers.dto.CustomerResponse;
import com.gaspar.cloudx.aws.domain.Customer;
import com.gaspar.cloudx.aws.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.services.sns.SnsClient;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"integrationTest"})
public class CustomerControllerIntegrationTest extends IntegrationTestInitializer {

    private static final Customer TEST_CUSTOMER = Customer.builder()
            .email("tamas.gaspar@gmail.com")
            .name("Tamas Gaspar")
            .id(UUID.randomUUID().toString())
            .build();

    private static final String CUSTOMER_URL = "/api/v1/customers";
    private static final String CUSTOMER_ID_URL = CUSTOMER_URL + "/{id}";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SnsClient snsClient;

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        customerRepository.createOrUpdateCustomer(TEST_CUSTOMER);
    }

    @AfterEach
    public void tearDown() {
        customerRepository.deleteCustomer(TEST_CUSTOMER.getId());
    }

    @Test
    public void shouldNotCreateInvalidCustomer() throws Exception {
        var invalidCustomer = Customer.builder()
                .email("")
                .name("No name")
                .build();
        mockMvc.perform(post(CUSTOMER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(invalidCustomer)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCreateCustomer() throws Exception {
        var customer = Customer.builder()
                .email("some@body.com")
                .name("Some Body")
                .build();
        String responseAsString = mockMvc.perform(post(CUSTOMER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getContentAsString();

        CustomerResponse customerResponse = jsonMapper.readValue(responseAsString, CustomerResponse.class);
        var subscriptionsResponse = snsClient.listSubscriptions();
        assertEquals(1, subscriptionsResponse.subscriptions().size());

        deleteCustomerRequest(customerResponse.getId());
    }


    @Test
    public void shouldNotGetNotExistingCustomer() throws Exception {
            mockMvc.perform(get(CUSTOMER_ID_URL, UUID.randomUUID().toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetCustomer() throws Exception {
        mockMvc.perform(get(CUSTOMER_ID_URL, TEST_CUSTOMER.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotDeleteNotExistingCustomer() throws Exception {
        mockMvc.perform(delete(CUSTOMER_ID_URL, UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteCustomer() throws Exception {
        deleteCustomerRequest(TEST_CUSTOMER.getId());
    }

    private void deleteCustomerRequest(String id) throws Exception {
        mockMvc.perform(delete(CUSTOMER_ID_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
