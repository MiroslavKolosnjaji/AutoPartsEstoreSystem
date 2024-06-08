package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.customer.CreateCustomerDTO;
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
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("Create user")
    @Test
    @Order(3)
    void testCreateCustomer_whenValidDetailsProvided_returns201StatusCode() throws Exception {

        CreateCustomerDTO createCustomerDTO = getTestCustomerDTO();


        mockMvc.perform(post(CustomerController.CUSTOMER_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCustomerDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", CustomerController.CUSTOMER_URI + "/4"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value(createCustomerDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(createCustomerDTO.getLastName()))
                .andExpect(jsonPath("$.address").value(createCustomerDTO.getAddress()))
                .andExpect(jsonPath("$.email").value(createCustomerDTO.getEmail()))
                .andExpect(jsonPath("$.phone").value(createCustomerDTO.getPhone()))
                .andExpect(jsonPath("$.city.id").value(createCustomerDTO.getCity().getId()))
                .andExpect(jsonPath("$.city.name").value(createCustomerDTO.getCity().getName()))
                .andExpect(jsonPath("$.city.zipCode").value(createCustomerDTO.getCity().getZipCode()));

    }

    @DisplayName("Create customer failed - invalid detail provided - returns code 400")
    @Test
    @Order(99)
    void testCreateCustomer_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {
        CreateCustomerDTO createCustomerDTO = getTestCustomerDTO();
        createCustomerDTO.setAddress("");

        mockMvc.perform(post(CustomerController.CUSTOMER_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCustomerDTO)))
                .andExpect(status().isBadRequest());

    }

    @DisplayName("Update Customer")
    @Test
    @Order(4)
    void testUpdateCustomer_whenValidDetailsProvided_returns204StatusCode() throws Exception {
        CreateCustomerDTO createCustomerDTO = getTestCustomerDTO();
        createCustomerDTO.setId(4L);
        createCustomerDTO.setAddress("1019 Thunder Road");

        mockMvc.perform(put(CustomerController.CUSTOMER_URI_WITH_ID, createCustomerDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCustomerDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Get Customer by ID")
    @Test
    @Order(1)
    void testGetCustomerById_whenValidIdProvided_returnsCustomerDTO() throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Get Customer failed - invalid ID provided - returns code 404")
    @Test
    void testGetCustomerById_whenInvalidIdProvided_returns404StatusCode() throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get All Customers")
    @Test
    @Order(2)
    void testGetCustomers_whenListIsPopulated_returnsListOfCustomerDTO() throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Delete customer")
    @Test
    @Order(5)
    void testDeleteCustomer_whenValidIdProvided_returns204StatusCode() throws Exception {

        mockMvc.perform(delete(CustomerController.CUSTOMER_URI_WITH_ID, 4)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Customer failed - invalid id provided - returns 404 status code")
    @Test
    void testDeleteCustomer_whenInvalidIdProvided_returns404StatusCode() throws Exception {
        mockMvc.perform(delete(CustomerController.CUSTOMER_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private CreateCustomerDTO getTestCustomerDTO() {
        return   CreateCustomerDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .address("1017 Thunder Road")
                .email("john@doe.com")
                .phone("+381324123565")
                .city(new City(1L, "Palo Alto", "94306"))
                .build();
    }
}