package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.customer.CustomerDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.CustomerNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.EmailAddressAlreadyExistsException;
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
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    public static final String CUSTOMER_URI = "/api/customer";
    public static final String CUSTOMER_ID = "/{customer_id}";
    public static final String CUSTOMER_URI_WITH_ID = CUSTOMER_URI + CUSTOMER_ID;

    private final CustomerService customerService;

    @PostMapping()
    public ResponseEntity<CustomerDTO> createCustomer(@Validated @RequestBody CustomerDTO customerDTO) {

        try {
            CustomerDTO saved = customerService.save(customerDTO);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Location", CUSTOMER_URI + "/" + saved.getId());

            return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
        } catch (EmailAddressAlreadyExistsException e) {
            throw new EntityAlreadyExistsException(e.getMessage());
        }
    }

    @PutMapping(CUSTOMER_ID)
    public ResponseEntity<Void> updateCustomer(@PathVariable("customer_id") Long customerId, @Validated @RequestBody CustomerDTO updateCustomerDTO) {

        try {

            customerService.update(customerId, updateCustomerDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (CustomerNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {

        List<CustomerDTO> customers = customerService.getAll();

        if (customers.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(customers, HttpStatus.OK);

    }

    @GetMapping(CUSTOMER_ID)
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("customer_id") Long customerId) {

        try {

            CustomerDTO customerDTO = customerService.getById(customerId);
            return ResponseEntity.ok(customerDTO);

        } catch (CustomerNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping(CUSTOMER_ID)
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customer_id") Long customerId) {

        try {

            customerService.delete(customerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (CustomerNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}
