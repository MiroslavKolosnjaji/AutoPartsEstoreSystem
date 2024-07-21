package com.myproject.autopartsestoresystem.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class CustomerTest {

    @Test
    void testAllFields_IsEqual_thenCorrect() {

        //given
        Customer customer1 = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .address("1017 Thunder Road")
                .email("john@doe.com")
                .phone("+381324123565")
                .city(new City(1L, "Palo Alto", "94306"))
                .build();

        Customer customer2 = Customer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .address("1017 Thunder Road")
                .email("john@doe.com")
                .phone("+381324123565")
                .city(new City(1L, "Palo Alto", "94306"))
                .build();

        boolean equals = customer1.equals(customer2);
        boolean hashCode = customer1.hashCode() == customer2.hashCode();

        assertAll("Customer fields validation",
                () -> assertEquals(customer1.getId(), customer2.getId(), "Customer ID mismatch"),
                () -> assertEquals(customer1.getFirstName(), customer2.getFirstName(), "Customer firstName mismatch"),
                () -> assertEquals(customer1.getLastName(), customer2.getLastName(), "Customer lastName mismatch"),
                () -> assertEquals(customer1.getAddress(), customer2.getAddress(), "Customer address mismatch"),
                () -> assertEquals(customer1.getEmail(), customer2.getEmail(), "Customer email mismatch"),
                () -> assertEquals(customer1.getPhone(), customer2.getPhone(), "Customer phone mismatch"),
                () -> assertEquals(customer1.getCity(), customer2.getCity(), "Customer city mismatch"));

        assertTrue(equals, "Customer should be equal");
        assertTrue(hashCode, "Customer hashCode mismatch");
    }
}