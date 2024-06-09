package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.customer.CustomerDTO;

import java.util.List;
import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface CustomerService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO);
    List<CustomerDTO> getCustomers();
    Optional<CustomerDTO> getCustomer(Long id);
    void deleteCustomer(Long id);
}
