package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.customer.CustomerDTO;
import com.myproject.autopartsestoresystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    public static final String CUSTOMER_URI = "/api/customer";
    public static final String CUSTOMER_ID = "/{customer_id}";
    public static final String CUSTOMER_URI_WITH_ID = CUSTOMER_URI + CUSTOMER_ID;

    private final CustomerService customerService;

    @PostMapping()
    public ResponseEntity<CustomerDTO> createCustomer(@Validated @RequestBody CustomerDTO customerDTO) {

        CustomerDTO saved = customerService.saveCustomer(customerDTO);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Location", CUSTOMER_URI + "/" + saved.getId());

        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @PutMapping(CUSTOMER_ID)
    public ResponseEntity<Void> updateCustomer(@PathVariable("customer_id") Long customerId, @Validated @RequestBody CustomerDTO updateCustomerDTO) {

        customerService.updateCustomer(customerId, updateCustomerDTO);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {

        List<CustomerDTO> customers = customerService.getCustomers();

        if(customers.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(customers);

    }

    @GetMapping(CUSTOMER_ID)
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("customer_id") Long customerId) {

        CustomerDTO customerDTO = customerService.getCustomer(customerId);

        return ResponseEntity.ok(customerDTO);
    }

    @DeleteMapping(CUSTOMER_ID)
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customer_id") Long customerId) {

        customerService.deleteCustomer(customerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
