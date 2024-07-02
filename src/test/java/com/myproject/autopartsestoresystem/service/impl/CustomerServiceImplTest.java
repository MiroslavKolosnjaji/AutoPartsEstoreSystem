package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.CustomerDTO;
import com.myproject.autopartsestoresystem.exception.service.CustomerNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.EmailAddressAlreadyExistsException;
import com.myproject.autopartsestoresystem.mapper.CustomerMapper;
import com.myproject.autopartsestoresystem.model.City;
import com.myproject.autopartsestoresystem.model.Customer;
import com.myproject.autopartsestoresystem.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {


    @Mock
    private CustomerRepository customerRepository;

    @Mock
    CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;


    CustomerDTO customerDTOExpected;
    CustomerDTO customerDTO;
    @BeforeEach
    void setUp() {
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

    @DisplayName("Save customer")
    @Test
    void testSaveCustomer_whenValidDetailsProvided_returnsCustomerDTO() {

        //given
        Customer customer = mock(Customer.class);

        when(customerMapper.customerDTOToCustomer(customerDTO)).thenReturn(customer);
        when(customerMapper.customerToCustomerDTO(customer)).thenReturn(customerDTOExpected);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        //when
        CustomerDTO savedDTO = customerService.save(customerDTO);

        //then
        assertNotNull(savedDTO, "Saved customer should not be null");
        assertEquals(customerDTOExpected, savedDTO);

        verify(customerMapper).customerDTOToCustomer(customerDTO);
        verify(customerMapper).customerToCustomerDTO(customer);
        verify(customerRepository).save(any(Customer.class));
    }

    @DisplayName("Save customer - failed (email already exists)")
    @Test
    void testSave_whenEmailAddressAlreadyExistsInSystem_throwsEmailAddressAlreadyExists() {

        //given
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(Customer.class)));

        //when
        Executable executable = () -> customerService.save(customerDTO);

        //then
        assertThrows(EmailAddressAlreadyExistsException.class, executable);

        verify(customerRepository).findByEmail(anyString());
    }

    @DisplayName("Update Customer")
    @Test
    void testUpdateCustomer_whenValidDetailsProvided_returnsUpdateDTO() {
        //given
        Customer customer = mock(Customer.class);

        when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));
        when(customerMapper.customerToCustomerDTO(customer)).thenReturn(customerDTOExpected);

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        //when
        CustomerDTO updatedDTO = customerService.update(any(Long.class), customerDTO);

        //then
        assertNotNull(updatedDTO, "Updated customer should not be null");
        assertEquals(customerDTOExpected, updatedDTO);

        verify(customerRepository).findById(anyLong());
        verify(customerMapper).customerToCustomerDTO(any(Customer.class));
    }

    @DisplayName("Update Customer Failed - Invalid ID Provided")
    @Test
    void testUpdateCustomer_whenInvalidIdIsProvided_throwsDoesntExistException() {

        //given
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> customerService.update(anyLong(), customerDTO);

        //then
        assertThrows(CustomerNotFoundException.class, executable);

        verify(customerRepository).findById(anyLong());
    }

    @DisplayName("Get Customers")
    @Test
    void testGetAll_whenListIsNotEmpty_returnsListOfCustomerDTO() {

        //given
        List<Customer> customers = Arrays.asList(mock(Customer.class), mock(Customer.class), mock(Customer.class));
        when(customerRepository.findAll()).thenReturn(customers);

        //when
        List<CustomerDTO> customerDTOList = customerService.getAll();

        //then
        assertNotNull(customerDTOList, "List of Customers should not be null");
        assertFalse(customerDTOList.isEmpty(), "List of Customers should not be empty");
        assertEquals(3, customerDTOList.size(), "List of Customers should have 3 elements");

        verify(customerRepository).findAll();
    }


    @DisplayName("Get Customers - Empty List")
    @Test
    void testGetAll_whenListIsEmpty_returnsEmptyList() {

        //given
        List<Customer> customers = new ArrayList<>();
        when(customerRepository.findAll()).thenReturn(customers);

        //when
        List<CustomerDTO> customerDTOList = customerService.getAll();

        //then
        assertNotNull(customerDTOList, "List of Customers should not be null");
        assertTrue(customerDTOList.isEmpty(), "List of Customers should be empty");

        verify(customerRepository).findAll();
    }

    @DisplayName("Get Customer")
    @Test
    void testGetCustomer_whenValidIdIsProvided_returnsCustomerDTO() {

        //given
        Customer customer = Customer.builder().firstName(customerDTO.getFirstName())
                .lastName(customerDTO.getLastName())
                .address(customerDTO.getAddress())
                .email(customerDTO.getEmail())
                .phone(customerDTO.getPhone())
                .city(customerDTO.getCity())
                .build();

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerMapper.customerToCustomerDTO(any(Customer.class))).thenReturn(customerDTO);

        //when
        CustomerDTO foundCustomerDTO = customerService.getById(anyLong());

        //then
        assertNotNull(foundCustomerDTO, "foundCustomerDTO should not be null");
        assertAll("Validate found customer details",
                () -> assertEquals(customerDTO.getId(), foundCustomerDTO.getId(), "Id doesn't match"),
                () -> assertEquals(customerDTO.getFirstName(), foundCustomerDTO.getFirstName(), "First name doesn't match"),
                () -> assertEquals(customerDTO.getLastName(), foundCustomerDTO.getLastName(), "Last name doesn't match"),
                () -> assertEquals(customerDTO.getAddress(), foundCustomerDTO.getAddress(), "Address doesn't match"),
                () -> assertEquals(customerDTO.getEmail(), foundCustomerDTO.getEmail(), "Email doesn't match"),
                () -> assertEquals(customerDTO.getPhone(), foundCustomerDTO.getPhone(), "Phone doesn't match"),
                () -> assertEquals(customerDTO.getCity(), foundCustomerDTO.getCity(), "City doesn't match"));

        verify(customerRepository).findById(anyLong());
        verify(customerMapper).customerToCustomerDTO(any(Customer.class));
    }

    @DisplayName("Get Customer - Invalid ID - Throws CustomerNotFound Exception ")
    @Test
    void testGetCustomer_whenInvalidIdProvided_throwsCustomerNotFoundException() {

        //given
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> customerService.getById(anyLong());

        //then
        assertThrows(CustomerNotFoundException.class, executable);
        verify(customerRepository).findById(anyLong());
    }

    @DisplayName("Delete Customer")
    @Test
    void testDelete_whenValidIdProvided_thenSuccess() {

        //given
        when(customerRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(customerRepository).deleteById(anyLong());

        //when
        customerService.delete(anyLong());

        //then
        verify(customerRepository).existsById(anyLong());
        verify(customerRepository).deleteById(anyLong());

    }

    @DisplayName("Delete Customer - Invalid ID - Throws CustomerNotFound Exception")
    @Test
    void testDeleteCustomer_whenInvalidIdProvided_throwsCustomerNotFoundException() {
        //given
        when(customerRepository.existsById(anyLong())).thenReturn(false);

        //when
        Executable executable = () -> customerService.delete(anyLong());

        //then
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, executable);
        assertEquals("Customer with id 0 does not exist", exception.getMessage());

        verify(customerRepository).existsById(anyLong());
    }

}