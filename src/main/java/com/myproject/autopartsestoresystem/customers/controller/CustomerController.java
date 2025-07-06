package com.myproject.autopartsestoresystem.customers.controller;

import com.myproject.autopartsestoresystem.customers.dto.CustomerDTO;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.common.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.security.permission.customer.CustomerCreatePermission;
import com.myproject.autopartsestoresystem.security.permission.customer.CustomerDeletePermission;
import com.myproject.autopartsestoresystem.security.permission.customer.CustomerReadPermission;
import com.myproject.autopartsestoresystem.security.permission.customer.CustomerUpdatePermission;
import com.myproject.autopartsestoresystem.customers.service.CustomerService;
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

    @CustomerCreatePermission
    @PostMapping()
    public ResponseEntity<CustomerDTO> createCustomer(@Validated @RequestBody CustomerDTO customerDTO) throws EntityAlreadyExistsException{

            CustomerDTO saved = customerService.save(customerDTO);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Location", CUSTOMER_URI + "/" + saved.getId());

            return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);

    }

    @CustomerUpdatePermission
    @PutMapping(CUSTOMER_ID)
    public ResponseEntity<Void> updateCustomer(@PathVariable("customer_id") Long customerId, @Validated @RequestBody CustomerDTO updateCustomerDTO) throws EntityNotFoundException, EntityAlreadyExistsException {

            customerService.update(customerId, updateCustomerDTO);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @CustomerReadPermission
    @GetMapping()
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {

        List<CustomerDTO> customers = customerService.getAll();

        if (customers.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(customers, HttpStatus.OK);

    }

    @CustomerReadPermission
    @GetMapping(CUSTOMER_ID)
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("customer_id") Long customerId) throws EntityNotFoundException {

            CustomerDTO customerDTO = customerService.getById(customerId);
            return ResponseEntity.ok(customerDTO);

    }

    @CustomerDeletePermission
    @DeleteMapping(CUSTOMER_ID)
    public ResponseEntity<Void> deleteCustomer(@PathVariable("customer_id") Long customerId) throws EntityNotFoundException {

            customerService.delete(customerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
