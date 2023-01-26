package com.epam.cloudx.aws.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.epam.cloudx.aws.controllers.dto.CustomerRequest;
import com.epam.cloudx.aws.domain.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MapperTestConfig.class)
public class CustomerMapperTest {

    @Autowired
    private CustomerMapper mapper;

    @Test
    public void shouldMapRequestToCustomer() {
        CustomerRequest request = new CustomerRequest();
        request.setEmail("a@b.com");
        request.setName("John");

        Customer customer = mapper.mapCustomerRequest(request);
        assertNotNull(customer.getId());
        assertEquals(request.getName(), customer.getName());
        assertEquals(request.getEmail(), customer.getEmail());
    }

}
