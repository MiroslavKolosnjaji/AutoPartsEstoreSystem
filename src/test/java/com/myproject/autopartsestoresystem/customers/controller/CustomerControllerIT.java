package com.myproject.autopartsestoresystem.customers.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.controller.BaseIT;
import com.myproject.autopartsestoresystem.customers.dto.CustomerDTO;
import com.myproject.autopartsestoresystem.cities.entity.City;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerControllerIT extends BaseIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("Create Customer")
    @Order(3)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreateCustomer_whenValidDetailsProvided_returns201StatusCode(String user, String password) throws Exception {

        CustomerDTO customerDTO = getTestCustomerDTO();

        mockMvc.perform(post(CustomerController.CUSTOMER_URI)
                        .with(httpBasic(user, password))
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
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreateCustomer_whenInvalidDetailsProvided_returns400StatusCode(String user, String password) throws Exception {
        CustomerDTO customerDTO = getTestCustomerDTO();
        customerDTO.setAddress("");

        mockMvc.perform(post(CustomerController.CUSTOMER_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isBadRequest());

    }

    @DisplayName("Create Customer Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testCreateCustomer_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        CustomerDTO customerDTO = getTestCustomerDTO();

        mockMvc.perform(post(CustomerController.CUSTOMER_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isForbidden());

    }

    @DisplayName("Create Customer Failed - No Role - Returns Code 401")
    @Test
    void testCreateCustomer_withoutAnyRole_returns401StatusCode() throws Exception {

        CustomerDTO customerDTO = getTestCustomerDTO();

        mockMvc.perform(post(CustomerController.CUSTOMER_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Update Customer")
    @Order(4)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testUpdateCustomer_whenValidDetailsProvided_returns204StatusCode(String user, String password) throws Exception {

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
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Update Customer Failed - No Role - Returns Code 401")
    @Test
    void testUpdateCustomer_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(put(CustomerController.CUSTOMER_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get Customer By ID")
    @Order(1)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testGetCustomerById_whenValidIdProvided_returnsCustomerDTO(String user, String password) throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Get Customer By ID Failed - Invalid ID Provided - Returns Code 404")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testGetCustomerById_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get Customer By ID Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testGetCustomerById_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(CustomerController.CUSTOMER_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetCustomerById_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(CustomerController.CUSTOMER_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get All Customers")
    @Order(2)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testGetCustomers_whenListIsPopulated_returnsListOfCustomerDTO(String user, String password) throws Exception {
        mockMvc.perform(get(CustomerController.CUSTOMER_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Get All Customers Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testGetCustomers_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(CustomerController.CUSTOMER_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllCustomers_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(CustomerController.CUSTOMER_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Delete Customer")
    @Order(5)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeleteCustomer_whenValidIdProvided_returns204StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(CustomerController.CUSTOMER_URI_WITH_ID, 4)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Customer Failed - Invalid ID Provided - Returns 404 Status Code")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_USER)
    void testDeleteCustomer_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {
        mockMvc.perform(delete(CustomerController.CUSTOMER_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete Customer Failed - Moderator Role - Returns 403 Status Code")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_MODERATOR_USER)
    void testDeleteCustomer_withModeratorRole_returns403StatusCode(String user, String password) throws Exception {
        mockMvc.perform(delete(CustomerController.CUSTOMER_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }


    @Test
    void testDeleteCustomer_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(delete(CustomerController.CUSTOMER_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private CustomerDTO getTestCustomerDTO() {
        return CustomerDTO.builder()
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