package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.customer.CreateCustomerDTO;
import com.myproject.autopartsestoresystem.dto.customer.CustomerDTO;
import com.myproject.autopartsestoresystem.dto.customer.UpdateCustomerDTO;

import java.util.List;
import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface CustomerService {

    CreateCustomerDTO saveCustomer(CreateCustomerDTO createCustomerDTO);
    UpdateCustomerDTO updateCustomer(Long customerId, UpdateCustomerDTO updateCustomerDTO);
    List<CustomerDTO> getCustomers();
    Optional<CustomerDTO> getCustomer(Long id);
    void deleteCustomer(Long id);
}
