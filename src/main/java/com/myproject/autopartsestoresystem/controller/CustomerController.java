package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.customer.CreateCustomerDTO;

import com.myproject.autopartsestoresystem.dto.customer.CustomerDTO;
import com.myproject.autopartsestoresystem.dto.customer.UpdateCustomerDTO;
import com.myproject.autopartsestoresystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@RestController
@RequestMapping("api/customer")
@RequiredArgsConstructor
public class CustomerController {

    public static final String CUSTOMER_ID = "/{customer_id}";

    private final CustomerService customerService;

    @PostMapping()
    public ResponseEntity<CreateCustomerDTO> createCustomer(@Validated @RequestBody CreateCustomerDTO createCustomerDTO) {

        CreateCustomerDTO saved = customerService.saveCustomer(createCustomerDTO);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Location", "/customer/" + saved.getId());

        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(CUSTOMER_ID)
    public ResponseEntity<Void> updateCustomer(@PathVariable("customer_id") Long customerId, @Validated @RequestBody UpdateCustomerDTO updateCustomerDTO) {

        customerService.updateCustomer(customerId, updateCustomerDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping(CUSTOMER_ID)
    public CustomerDTO getCustomerById(@PathVariable("customer_id") Long customerId) {
        return customerService.getCustomer(customerId);
    }

    @DeleteMapping(CUSTOMER_ID)
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customer_id") Long customerId) {

        customerService.deleteCustomer(customerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
