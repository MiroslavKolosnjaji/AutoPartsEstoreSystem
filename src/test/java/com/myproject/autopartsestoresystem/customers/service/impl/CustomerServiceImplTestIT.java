package com.myproject.autopartsestoresystem.customers.service.impl;


import com.myproject.autopartsestoresystem.customers.dto.CustomerDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.customers.exception.CustomerNotFoundException;
import com.myproject.autopartsestoresystem.model.City;
import com.myproject.autopartsestoresystem.repository.CityRepository;
import com.myproject.autopartsestoresystem.customers.service.CustomerService;
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
    void testSave_whenValidDetailsProvided() throws EntityAlreadyExistsException {

        //given


        //when
        CustomerDTO savedDTO = customerService.save(customerDTO);

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
    void testUpdateCustomer_whenCustomerNameChanged_returnsUpdated() throws EntityAlreadyExistsException, EntityNotFoundException {

        //given
        CustomerDTO savedDTO = customerService.save(customerDTO);
        savedDTO.setFirstName("Matthew");

        //when
        CustomerDTO updatedDTO = customerService.update(savedDTO.getId(), savedDTO);

        //then
        assertNotNull(updatedDTO, "Updated Customer should not be null");
        assertEquals(savedDTO.getFirstName(), updatedDTO.getFirstName(), "First name does not match");
    }

    @DisplayName("Get All Customers")
    @Test
    void testGetAllCustomers_whenListIsNotEmpty_returnsTwoCustomers() throws EntityAlreadyExistsException {

        //given (there are already 3 customers saved because of bootstrap data)
        customerService.save(customerDTO);

        //when
        List<CustomerDTO> customers = customerService.getAll();

        //then
        assertNotNull(customers, "Customer list should not be null");
        assertEquals(4, customers.size(), "Customer list size should be 4 after adding one more customer to the database");
    }

    @DisplayName("Get Customer")
    @Test
    void testGetCustomer_whenValidIdProvided_returnsById() throws EntityAlreadyExistsException, EntityNotFoundException {

        //given
        CustomerDTO savedDTO = customerService.save(customerDTO);

        //when
        CustomerDTO foundCustomer = customerService.getById(savedDTO.getId());

        assertNotNull(foundCustomer);
        assertEquals(savedDTO.getId(), foundCustomer.getId(), "Id does not match");

    }

    @DisplayName("Delete Customer")
    @Test
    void testDelete_whenValidIdProvided_thenCorrect() throws EntityAlreadyExistsException, EntityNotFoundException {

        //given
        CustomerDTO savedCustomer = customerService.save(customerDTO);

        //when
        customerService.delete(savedCustomer.getId());
        Executable executable = () -> customerService.getById(savedCustomer.getId());

        //then
        assertThrows(CustomerNotFoundException.class, executable);
    }
}