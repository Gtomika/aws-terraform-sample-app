package com.epam.cloudx.aws.mappers;

import com.epam.cloudx.aws.controllers.dto.CustomerRequest;
import com.epam.cloudx.aws.controllers.dto.CustomerResponse;
import com.epam.cloudx.aws.domain.Customer;
import java.util.UUID;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    default Customer mapCustomerRequest(CustomerRequest request) {
        return Customer.builder()
                .id(createCustomerId())
                .name(request.getName())
                .email(request.getEmail())
                .build();
    }

    CustomerResponse mapCustomerResponse(Customer customer);

    default String createCustomerId() {
        return UUID.randomUUID().toString();
    }
}
