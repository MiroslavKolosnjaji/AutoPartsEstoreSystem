package com.myproject.autopartsestoresystem.customers.dto;

import com.myproject.autopartsestoresystem.cities.entity.City;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class CustomerDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<CustomerDTO>> violations;
    private ConstraintViolation<CustomerDTO> violation;
    private CustomerDTO customerDTO;

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
    }

    public CustomerDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @DisplayName("First Name - valid input")
    @Test
    void testFirstNameValidation_whenValidInputProvided_thenCorrect() {

        //given
        customerDTO.setFirstName("John");

        //when
        violations = validator.validate(customerDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid first name");
    }

    @DisplayName("First Name - null - invalid input")
    @Test
    void testFirstNameValidation_whenInputIsNull_thenValidationFailed() {

        //given
        customerDTO.setFirstName(null);

        //when
        violations = validator.validate(customerDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for first name that is null");

        violation = violations.iterator().next();
        assertEquals("First name cannot be null", violation.getMessage());
    }




    @DisplayName("First Name - lest than 2 char - invalid input")
    @Test
    void testFirstNameValidation_whenInputHaveLessThan2Characters_thenValidationFailed() {

        //given
        customerDTO.setFirstName("A");

        //when
        violations = validator.validate(customerDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for first name less than 2 characters");

        violation = violations.iterator().next();
        assertEquals("First name must be between 2 and 50 characters", violation.getMessage());
    }

    @DisplayName("First Name - more than 50 char - invalid input")
    @Test
    void testFirstNameValidation_whenInputHaveMoreThan50Characters_thenValidationFailed() {

        //given
        customerDTO.setFirstName("LKASJDLKAJDLASJDLOGIHSOIDFJDFLKSDJFLSKDJFLSDFHASJHSJASHDLASJHDLAKSDASDKASLDKHASLD");

        //when
        violations = validator.validate(customerDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for first name longer than 50 characters");

        violation = violations.iterator().next();
        assertEquals("First name must be between 2 and 50 characters", violation.getMessage());
    }

    @DisplayName("Last Name - valid input")
    @Test
    void testLastName_whenValidInputProvided_thenCorrect() {

        //given
        customerDTO.setLastName("Doe");

        //when
        violations = validator.validate(customerDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid last name");
    }

    @DisplayName("Last name - null - invalid input")
    @Test
    void testLastName_whenInputIsNull_thenValidationFailed() {

        //given
        customerDTO.setLastName(null);

        //when
        violations = validator.validate(customerDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for Last name that is null");

        violation = violations.iterator().next();
        assertEquals("Last name cannot be null", violation.getMessage());
    }

    @DisplayName("Last name - less than 2 char - invalid input")
    @Test
    void testLastName_whenInputHaveLessThan2Characters_thenValidationFailed() {

        //given
        customerDTO.setLastName("L");

        //when
        violations = validator.validate(customerDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for Last name less than 2 characters");

        violation = violations.iterator().next();
        assertEquals("Last name must have minimum 2 characters", violation.getMessage());
    }

    @DisplayName("Address - valid input")
    @Test
    void testAddress_whenValidInputProvided_thenCorrect() {

        //given
        customerDTO.setAddress("1017 Thunder Road");

        //when
        violations = validator.validate(customerDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid address");
    }

    @DisplayName("Address - null - invalid input")
    @Test
    void testAddress_whenInputIsNull_thenValidationFailed() {

        //given
        customerDTO.setAddress(null);

        //when
        violations = validator.validate(customerDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for address that is null");

        violation = violations.iterator().next();
        assertEquals("Address cannot be blank", violation.getMessage());
    }

    @DisplayName("Address - blank - invalid input")
    @Test
    void testAddress_whenBlankInputProvided_thenValidationFailed() {

        //given
        customerDTO.setAddress("");

        //when
        violations = validator.validate(customerDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for address blank");

        violation = violations.iterator().next();
        assertEquals("Address cannot be blank", violation.getMessage());
    }

    @DisplayName("Email - valid input")
    @Test
    void testEmail_whenValidInputProvided_thenCorrect() {

        //given
        customerDTO.setEmail("john@doe.com");

        //when
        violations = validator.validate(customerDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid email");
    }

    @DisplayName("Email - null - invalid input")
    @Test
    public void testEmail_whenInputIsNull_thenValidationFailed() {

        //given
        customerDTO.setEmail(null);

        //when
        violations = validator.validate(customerDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for email that is null");

        violation = violations.iterator().next();
        assertEquals("Email cannot be blank", violation.getMessage());
    }

    @DisplayName("Email - invalid format input")
    @Test
    void testEmail_whenInvalidInputFormatProvided_thenValidationFailed() {

        //given
        customerDTO.setEmail("john.doe.com");

        //when
        violations = validator.validate(customerDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for email that have invalid format");

        violation = violations.iterator().next();
        assertEquals("Invalid email format", violation.getMessage());
    }

    @DisplayName("Phone - valid input")
    @Test
    void testPhone_whenValidInputProvided_thenCorrect() {

        //given
        customerDTO.setPhone("+381324123565");

        //when
        violations = validator.validate(customerDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid phone number");
    }

    @DisplayName("Phone - null - invalid input")
    @Test
    void testPhone_whenInputIsNull_thenValidationFailed() {

        //given
        customerDTO.setPhone(null);

        //when
        violations = validator.validate(customerDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for phone number that is null");

        violation = violations.iterator().next();
        assertEquals("Phone number cannot be null", violation.getMessage());
    }

    @Test
    void testPhone_whenBlankInputProvided_thenValidationFailed() {

        //given
        customerDTO.setPhone("");

        //when
        violations = validator.validate(customerDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for phone number that is blank");

        violation = violations.iterator().next();
        assertEquals("Invalid phone number format", violation.getMessage());
    }

    @Test
    void testCity_whenValidInputProvided_thenCorrect() {

        //given
        customerDTO.setCity(new City(1L, "Palo Alto", "94306"));

        //when
        violations = validator.validate(customerDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for city");
    }


    @Test
    void testCity_whenInputIsNull_thenValidationFailed() {

        //given
        customerDTO.setCity(null);

        //when
        violations = validator.validate(customerDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for city that is null");

        violation = violations.iterator().next();
        assertEquals("City cannot be null", violation.getMessage());
    }

    @Test
    void testIsEqual_whenCompareTwoObjectsWithSameDetails_thenIsEqual() {
        CustomerDTO customerDTO2 = CustomerDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .address("1017 Thunder Road")
                .email("john@doe.com")
                .phone("+381324123565")
                .city(new City(1L, "Palo Alto", "94306"))
                .build();

        //when
        boolean isEqual = customerDTO2.equals(customerDTO);
        boolean isEqualHashCode = customerDTO2.hashCode() == customerDTO.hashCode();

        assertTrue(isEqual, "CustomerDTO should be equal");
        assertTrue(isEqualHashCode, "CustomerDTO hashCode should be equal");
    }
}