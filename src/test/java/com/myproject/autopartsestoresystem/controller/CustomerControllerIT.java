package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.CustomerDTO;
import com.myproject.autopartsestoresystem.model.City;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Miroslav Kološnjaji
 */
@Disabled("Class is disabled pending security updates")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("Create Customer")
    @Order(3)
    @Test
    void testCreateCustomer_whenValidDetailsProvided_returns201StatusCode() throws Exception {

        CustomerDTO customerDTO = getTestCustomerDTO();


        mockMvc.perform(post(CustomerController.CUSTOMER_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", CustomerController.CUSTOMER_URI + "/4"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(customerDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(customerDTO.getLastName()))
                .andExpect(jsonPath("$.address").value(customerDTO.getAddress()))
                .andExpect(jsonPath("$.email").value(customerDTO.getEmail()))
                .andExpect(jsonPath("$.phone").value(customerDTO.getPhone()))
                .andExpect(jsonPath("$.cards").value(customerDTO.getCards()))
                .andExpect(jsonPath("$.city.id").value(customerDTO.getCity().getId()))
                .andExpect(jsonPath("$.city.name").value(customerDTO.getCity().getName()))
                .andExpect(jsonPath("$.city.zipCode").value(customerDTO.getCity().getZipCode()));

    }

    @DisplayName("Create Customer Failed - Invalid Details Provided - Returns Code 400")
    @Order(99)
    @Test
    void testCreateCustomer_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {
        CustomerDTO customerDTO = getTestCustomerDTO();
        customerDTO.setAddress("");

        mockMvc.perform(post(CustomerController.CUSTOMER_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());

    }

    @DisplayName("Update Customer")
    @Order(4)
    @Test
    void testUpdateCustomer_whenValidDetailsProvided_returns204StatusCode() throws Exception {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(4L)
                .firstName("John")
                .lastName("Doe")
                .address("1019 Thunder Road")
                .email("john@doe.com")
                .phone("+381324123565")
                .city(new City(1L, "Palo Alto", "94306"))
                .build();


        mockMvc.perform(put(CustomerController.CUSTOMER_URI_WITH_ID, customerDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Get Customer By ID")
    @Order(1)
    @Test
    void testGetCustomerById_whenValidIdProvided_returnsCustomerDTO() throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Get Customer By ID Failed - Invalid ID Provided - Returns Code 404")
    @Test
    void testGetCustomerById_whenInvalidIdProvided_returns404StatusCode() throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get All Customers")
    @Order(2)
    @Test
    void testGetCustomers_whenListIsPopulated_returnsListOfCustomerDTO() throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Delete Customer")
    @Order(5)
    @Test
    void testDeleteCustomer_whenValidIdProvided_returns204StatusCode() throws Exception {

        mockMvc.perform(delete(CustomerController.CUSTOMER_URI_WITH_ID, 4)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Customer Failed - Invalid ID Provided - Returns 404 Status Code")
    @Test
    void testDeleteCustomer_whenInvalidIdProvided_returns404StatusCode() throws Exception {
        mockMvc.perform(delete(CustomerController.CUSTOMER_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private CustomerDTO getTestCustomerDTO() {
        return   CustomerDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .address("1017 Thunder Road")
                .email("john@doe.com")
                .phone("+381324123565")
                .city(new City(1L, "Palo Alto", "94306"))
                .cards(null)
                .build();
    }
}