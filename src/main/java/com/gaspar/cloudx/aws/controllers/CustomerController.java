package com.gaspar.cloudx.aws.controllers;

import com.gaspar.cloudx.aws.controllers.dto.CustomerRequest;
import com.gaspar.cloudx.aws.controllers.dto.CustomerResponse;
import com.gaspar.cloudx.aws.domain.Customer;
import com.gaspar.cloudx.aws.mappers.CustomerMapper;
import com.gaspar.cloudx.aws.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(@PathVariable String id) {
        return customerMapper.mapCustomerResponse(customerService.getCustomer(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(@RequestBody CustomerRequest request) {
        Customer customer = customerMapper.mapCustomerRequest(request);
        log.debug("Generated ID for new customer: {}", customer.getId());
        return customerMapper.mapCustomerResponse(customerService.createCustomer(customer));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable String id) {
        customerService.deleteCustomer(id);
    }
}
