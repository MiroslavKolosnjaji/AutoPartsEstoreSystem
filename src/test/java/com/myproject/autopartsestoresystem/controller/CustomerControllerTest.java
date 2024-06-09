package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.customer.CustomerDTO;
import com.myproject.autopartsestoresystem.exception.CustomerDoesntExistsException;
import com.myproject.autopartsestoresystem.model.City;
import com.myproject.autopartsestoresystem.service.CustomerService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@WebMvcTest(value = CustomerController.class)
@MockBean({CustomerService.class})
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CustomerService customerService;

    ObjectMapper objectMapper;

    CustomerDTO customerDTOExpected;

    CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();


        customerDTO = CustomerDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .address("1017 Thunder Road")
                .email("john@doe.com")
                .phone("+381324123565")
                .city(new City(1L, "Palo Alto", "94306"))
                .build();

        customerDTOExpected = CustomerDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .address("1017 Thunder Road")
                .email("john@doe.com")
                .phone("+381324123565")
                .city(new City(1L, "Palo Alto", "94306"))
                .build();
    }

    @DisplayName("Customer created")
    @Test
    void testCreateCustomer_whenValidCustomerDetailsProvided_returnsCreatedCustomerDTO() throws Exception {

        // given
        when(customerService.saveCustomer(any(CustomerDTO.class))).thenReturn(customerDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        CustomerDTO savedDTO = objectMapper.readValue(response, CustomerDTO.class);


        assertAll("Saved Customer validation",
                () -> assertEquals(customerDTO.getFirstName(), savedDTO.getFirstName(), "First name doesn't match"),
                () -> assertEquals(customerDTO.getLastName(), savedDTO.getLastName(), "Last name doesn't match"),
                () -> assertEquals(customerDTO.getAddress(), savedDTO.getAddress(), "Address doesn't match"),
                () -> assertEquals(customerDTO.getEmail(), savedDTO.getEmail(), "Email address doesn't match"),
                () -> assertEquals(customerDTO.getPhone(), savedDTO.getPhone(), "Phone number doesn't match"),
                () -> assertEquals(customerDTO.getCity(), savedDTO.getCity(), "City doesn't match"));

        verify(customerService).saveCustomer(any(CustomerDTO.class));


    }

    @DisplayName("Create customer with invalid customer details provided - return status 400")
    @Test
    void testCreateCustomer_whenFirstNameIsNotProvided_returns400StatusCode() throws Exception {

        //given
        customerDTO.setFirstName("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/customer")
                .content(objectMapper.writeValueAsString(customerDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Update customer")
    @Test
    void testUpdateCustomer_whenValidCustomerDetailsProvided_returns204StatusCode() throws Exception {

        //given
        customerDTO.setId(1L);
        customerDTO.setAddress("1029 Thunder Road");
        when(customerService.updateCustomer(any(Long.class), any(CustomerDTO.class))).thenReturn(customerDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/customer/{customer_id}", customerDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(customerService).updateCustomer(any(Long.class), any(CustomerDTO.class));

    }

    @DisplayName("Update customer with invalid customer details provided - return status 400")
    @Test
    void testUpdateCustomer_whenInvalidCustomerDetailsProvided_returns400StatusCode() throws Exception {

        //given
        customerDTO.setId(1L);
        customerDTO.setAddress("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/customer/{customer_id}", customerDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }


    @DisplayName("Get all customers")
    @Test
    void testGetCustomers_whenListIsPopulated_returnsTwoCustomers() throws Exception {

        //given
        List<CustomerDTO> customerDTOList = getCustomers();
        when(customerService.getCustomers()).thenReturn(customerDTOList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTOList));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        
        //then
        assertEquals(2, objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size());
        verify(customerService).getCustomers();

    }

    @DisplayName("Get customer with valid id")
    @Test
    void testGetCustomer_whenValidIdProvided_returnsCustomerWithId1() throws Exception {

        //given
        CustomerDTO customerDTO = getCustomers().get(0);

        when(customerService.getCustomer(any(Long.class))).thenReturn(Optional.of(customerDTO));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customer/{customerId}", customerDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO));
        
        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        CustomerDTO getCustomer = objectMapper.readValue(response, CustomerDTO.class);


        assertAll("Get Customer by id validation",
                () -> assertEquals(customerDTO.getId(), getCustomer.getId(),"Id doesn't match"),
                () -> assertEquals(customerDTO.getFirstName(), getCustomer.getFirstName(), "First name doesn't match"),
                () -> assertEquals(customerDTO.getLastName(), getCustomer.getLastName(), "Last name doesn't match"),
                () -> assertEquals(customerDTO.getAddress(), getCustomer.getAddress(), "Address doesn't match"),
                () -> assertEquals(customerDTO.getEmail(), getCustomer.getEmail(), "Email address doesn't match"),
                () -> assertEquals(customerDTO.getPhone(), getCustomer.getPhone(), "Phone number doesn't match"),
                () -> assertEquals(customerDTO.getCity(), getCustomer.getCity(), "City doesn't match"));

        verify(customerService).getCustomer(customerDTO.getId());

    }

    @DisplayName("Get customer with invalid id - returns status 404")
    @Test
    void testGetCustomer_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        Long id = 100L;

        when(customerService.getCustomer(any(Long.class))).thenReturn(Optional.empty());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/customer/{customerId}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        verify(customerService).getCustomer(any(Long.class));

    }
    @DisplayName("Delete customer")
    @Test
    void testDeleteCustomer_whenValidIdProvided_returns204StatusCode() throws Exception {

        //given
        Long id = 1L;
        doNothing().when(customerService).deleteCustomer(id);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/customer/{customerId}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(customerService).deleteCustomer(id);
    }

    @DisplayName("Delete customer with invalid Id - throws CustomerDoesntExists")
    @Test
    void testDeleteCustomer_whenInvalidIdProvided_throwsCustomerDoesntExistsAndreturns404StatusCode() throws Exception {

        //given
        final String MESSAGE = "Customer doesn't exists";
        Long invalidId = 100L;
        doThrow(new CustomerDoesntExistsException(MESSAGE)).when(customerService).deleteCustomer(invalidId);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/customer/{customerId}", invalidId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //then
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBody = result.getResponse().getContentAsString();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        assertEquals(responseBody, MESSAGE, "Error message doesn't match");
        verify(customerService).deleteCustomer(invalidId);

    }

    private List<CustomerDTO> getCustomers() {
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        customerDTOList.add(CustomerDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .address("1017 Thunder Road")
                .email("john@doe.com")
                .phone("+381324123565")
                .city(new City(1L, "Palo Alto", "94306"))
                .build());

        customerDTOList.add(CustomerDTO.builder()
                .id(2L)
                .firstName("Sarah")
                .lastName("Connor")
                .address("1190 Stiles Street")
                .email("sarahconnor@test.com")
                .phone("+4125718361")
                .city(new City(2L, "Pittsburgh", "15226"))
                .build());


        return customerDTOList;
    }
}