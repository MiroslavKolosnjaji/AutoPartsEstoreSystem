package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.customer.CreateCustomerDTO;
import com.myproject.autopartsestoresystem.dto.customer.CustomerDTO;
import com.myproject.autopartsestoresystem.dto.customer.UpdateCustomerDTO;
import com.myproject.autopartsestoresystem.model.Customer;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface CustomerMapper {

    CustomerDTO customerToCustomerDTO(Customer customer);
    Customer customerDTOToCustomer(CustomerDTO baseCustomerDTO);
    CreateCustomerDTO  customerToCreateCustomerDTO(Customer customer);
    Customer createCustomerDTOToCustomer(CreateCustomerDTO createCustomerDTO);
    UpdateCustomerDTO customerToUpdateCustomerDTO(Customer customer);
    Customer updateCustomerDTOToCustomer(UpdateCustomerDTO updateCustomerDTO);
}
