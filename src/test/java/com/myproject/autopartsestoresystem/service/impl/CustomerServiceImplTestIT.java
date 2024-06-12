package com.myproject.autopartsestoresystem.service.impl;


import com.myproject.autopartsestoresystem.dto.customer.CustomerDTO;
import com.myproject.autopartsestoresystem.exception.CustomerDoesntExistsException;
import com.myproject.autopartsestoresystem.model.City;
import com.myproject.autopartsestoresystem.model.Customer;
import com.myproject.autopartsestoresystem.repository.CityRepository;
import com.myproject.autopartsestoresystem.repository.CustomerRepository;
import com.myproject.autopartsestoresystem.service.CustomerService;
import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class CustomerServiceImplTestIT {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private CityRepository cityRepository;


    private CustomerDTO customerDTO;



    @BeforeEach
    void setUp() {

        City city = City.builder()
                .name("Test")
                .zipCode("12312")
                .build();

        cityRepository.save(city);

        customerDTO = CustomerDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .address("testAddress")
                .email("test@email.com")
                .city(city)
                .build();
    }

    @DisplayName("Save Customer With Valid Details")
    @Test
    void testSaveCustomer_whenValidDetailsProvided() {

        //given


        //when
        CustomerDTO savedDTO = customerService.saveCustomer(customerDTO);

        //then
        assertNotNull(savedDTO);
        assertAll("Validate details",
                () -> assertNotNull(savedDTO.getId(), "Id is null"),
                () -> assertTrue(savedDTO.getId() > 0, "Id is negative or zero"),
                () -> assertEquals(customerDTO.getFirstName(), savedDTO.getFirstName(), "First name does not match"),
                () -> assertEquals(customerDTO.getLastName(), savedDTO.getLastName()),
                () -> assertEquals(customerDTO.getAddress(), savedDTO.getAddress()),
                () -> assertEquals(customerDTO.getEmail(), savedDTO.getEmail()),
                () -> assertEquals(customerDTO.getCity(), savedDTO.getCity()));
    }

    @DisplayName("Update Customer")
    @Test
    void testUpdateCustomer_whenCustomerNameChanged_returnsUpdatedCustomer() {

        //given
        CustomerDTO savedDTO = customerService.saveCustomer(customerDTO);
        savedDTO.setFirstName("Matthew");

        //when
        CustomerDTO updatedDTO = customerService.updateCustomer(savedDTO.getId(), savedDTO);

        //then
        assertNotNull(updatedDTO, "Updated Customer should not be null");
        assertEquals(savedDTO.getFirstName(), updatedDTO.getFirstName(), "First name does not match");
    }

    @DisplayName("Get All Customers")
    @Test
    void testGetAllCustomers_whenListIsNotEmpty_returnsTwoCustomers() {

        //given (there are already 3 customers saved because of bootstrap data)
        customerService.saveCustomer(customerDTO);

        //when
        List<CustomerDTO> customers = customerService.getCustomers();

        //then
        assertNotNull(customers, "Customer list should not be null");
        assertEquals(4, customers.size(), "Customer list size should be 4 after adding one more customer to the database");
    }

    @DisplayName("Get Customer")
    @Test
    void testGetCustomer_whenValidIdProvided_returnsCustomer() {

        //given
        CustomerDTO savedDTO = customerService.saveCustomer(customerDTO);

        //when
        CustomerDTO foundCustomer = customerService.getCustomer(savedDTO.getId());

        assertNotNull(foundCustomer);
        assertEquals(savedDTO.getId(), foundCustomer.getId(), "Id does not match");

    }

    @DisplayName("Delete Customer")
    @Test
    void testDeleteCustomer_whenValidIdProvided_thenCorrect() {

        //given
        CustomerDTO savedCustomer = customerService.saveCustomer(customerDTO);

        //when
        customerService.deleteCustomer(savedCustomer.getId());
        Executable executable = () -> customerService.getCustomer(savedCustomer.getId());

        //then
        assertThrows(CustomerDoesntExistsException.class, executable);
    }
}