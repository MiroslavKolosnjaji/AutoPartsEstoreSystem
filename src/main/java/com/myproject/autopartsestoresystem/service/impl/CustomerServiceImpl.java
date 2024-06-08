package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.customer.CreateCustomerDTO;
import com.myproject.autopartsestoresystem.dto.customer.CustomerDTO;
import com.myproject.autopartsestoresystem.dto.customer.UpdateCustomerDTO;
import com.myproject.autopartsestoresystem.exception.CustomerDoesntExistsException;
import com.myproject.autopartsestoresystem.exception.EmailAddressAlreadyExistsException;
import com.myproject.autopartsestoresystem.mapper.CustomerMapper;
import com.myproject.autopartsestoresystem.model.Customer;
import com.myproject.autopartsestoresystem.repository.CustomerRepository;
import com.myproject.autopartsestoresystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    @Override
    public CreateCustomerDTO saveCustomer(CreateCustomerDTO createCustomerDTO) {

        if (customerRepository.findByEmail(createCustomerDTO.getEmail()).isPresent())
            throw new EmailAddressAlreadyExistsException();

        Customer saved = customerRepository.save(customerMapper.createCustomerDTOToCustomer(createCustomerDTO));

        return customerMapper.customerToCreateCustomerDTO(saved);
    }

    @Override
    public UpdateCustomerDTO updateCustomer(Long customer_id, UpdateCustomerDTO updateCustomerDTO) {
        Customer customer = customerRepository.findById(customer_id)
                .orElseThrow(CustomerDoesntExistsException::new);

        customer.setFirstName(updateCustomerDTO.getFirstName());
        customer.setLastName(updateCustomerDTO.getLastName());
        customer.setAddress(updateCustomerDTO.getAddress());
        customer.setEmail(updateCustomerDTO.getEmail());
        customer.setPhone(updateCustomerDTO.getPhone());
        customer.setCity(updateCustomerDTO.getCity());

        customerRepository.save(customer);

        return customerMapper.customerToUpdateCustomerDTO(customer);

    }

    @Override
    public List<CustomerDTO> getCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomer(Long id){
        Customer customer = customerRepository.findById(id).orElseThrow(CustomerDoesntExistsException::new);
        return Optional.of(customerMapper.customerToCustomerDTO(customer));
    }

    @Override
    public void deleteCustomer(Long id){

        if(!customerRepository.existsById(id))
            throw new CustomerDoesntExistsException("Invalid Customer ID");


        customerRepository.deleteById(id);
    }
}
