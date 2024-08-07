package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.CardDTO;
import com.myproject.autopartsestoresystem.dto.CustomerDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.CustomerNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.EmailAddressAlreadyExistsException;
import com.myproject.autopartsestoresystem.mapper.CardMapper;
import com.myproject.autopartsestoresystem.mapper.CustomerMapper;
import com.myproject.autopartsestoresystem.model.Customer;
import com.myproject.autopartsestoresystem.repository.CustomerRepository;
import com.myproject.autopartsestoresystem.service.CardService;
import com.myproject.autopartsestoresystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CardService cardService;
    private final CardMapper cardMapper;


    @Override
    public CustomerDTO save(CustomerDTO customerDTO) throws EmailAddressAlreadyExistsException, EntityAlreadyExistsException {

        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent())
            throw new EmailAddressAlreadyExistsException("Email address already exists");

        Customer saved = customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO));

        if (customerDTO.getCards() == null || customerDTO.getCards().isEmpty())
            return customerMapper.customerToCustomerDTO(saved);

        CardDTO customerCard = cardMapper.cardToCardDTO(customerDTO.getCards().get(0));

        cardService.save(customerCard);

        return customerMapper.customerToCustomerDTO(saved);
    }

    @Override
    public CustomerDTO update(Long customer_id, CustomerDTO customerDTO) throws CustomerNotFoundException{
        Customer customer = customerRepository.findById(customer_id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setAddress(customerDTO.getAddress());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setCity(customerDTO.getCity());

        customerRepository.save(customer);

        return customerMapper.customerToCustomerDTO(customer);

    }

    @Override
    public List<CustomerDTO> getAll() {
        return customerRepository.findAll().stream()
                .map(customerMapper::customerToCustomerDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getById(Long id) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        return customerMapper.customerToCustomerDTO(customer);
    }

    @Override
    public void delete(Long id) throws CustomerNotFoundException {

        if (!customerRepository.existsById(id))
            throw new CustomerNotFoundException(id);


        customerRepository.deleteById(id);
    }
}
