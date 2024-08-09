package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.City;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class StoreDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<StoreDTO>> violations;
    private ConstraintViolation<StoreDTO> violation;
    private StoreDTO storeDTO;

    public StoreDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        storeDTO = StoreDTO.builder()
                .name("Store XYZ")
                .email("xyz@example.com")
                .phoneNumber("123123123")
                .city(City.builder().build())
                .build();
    }

    @DisplayName("Name - Valid Input")
    @Test
    void testName_whenValidInputProvided_thenCorrect() {

        //given
        storeDTO.setName("TEST");

        //when
        violations = validator.validate(storeDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid name");
    }

    @DisplayName("Name - Blank Input")
    @Test
    void testName_whenBlankInputProvided_thenValidationFailed() {

        //given
        storeDTO.setName(" ");

        //when
        violations = validator.validate(storeDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for blank name");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Store name required");
    }

    @DisplayName("Name - Null Input")
    @Test
    void testName_whenInputIsNull_thenValidationFailed() {

        //given
        storeDTO.setName(null);

        //when
        violations = validator.validate(storeDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for null name");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Store name required");
    }

    @DisplayName("Phone Number - Valid Input")
    @Test
    void testPhoneNumber_whenValidInputProvided_thenCorrect() {

        //given
        storeDTO.setPhoneNumber("123123123");

        //when
        violations = validator.validate(storeDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid phone number");
    }

    @DisplayName("Phone Number - Blank Input")
    @Test
    void testPhoneNumber_whenBlankInputProvided_thenValidationFailed () {

        //given
        storeDTO.setPhoneNumber("");

        //when
        violations = validator.validate(storeDTO);
        assertFalse(violations.isEmpty(), "Violation should fail for blank phone number");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Invalid phone number format");
    }

    @DisplayName("Email - Valid Input")
    @Test
    void testEmail_whenValidInputProvided_thenCorrect() {

        //given
        storeDTO.setEmail("xyz@example.com");

        //when
        violations = validator.validate(storeDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid email");
    }

    @Test
    void testEmail_whenBlankInputProvided_thenValidationFailed() {

        //given
        storeDTO.setEmail(null);

        //when
        violations = validator.validate(storeDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for null email");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Email required");
    }

    @DisplayName("Email - Invalid Input")
    @Test
    void testEmail_whenIvalidInputProvided_thenValidationFailed() {

        //given
        storeDTO.setEmail("xyzexample.com");

        //when
        violations = validator.validate(storeDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for valid email");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "Invalid email format");
    }

    @DisplayName("City - Valid Input")
    @Test
    void testCity_whenValidInputProvided_thenCorrect() {

        //given
        storeDTO.setCity(City.builder().build());

        //when
        violations = validator.validate(storeDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid city");
    }

    @DisplayName("City - Null Input")
    @Test
    void testCity_whenInputIsNull_thenValidationFailed() {

        //given
        storeDTO.setCity(null);

        //when
        violations = validator.validate(storeDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for null city");

        violation = violations.iterator().next();
        assertEquals(violation.getMessage(), "City cannot be null");
    }

    @Test
    void testIsEqual_whenCompareTwoObjectsWithSameDetails_thenIsEqual() {

        StoreDTO storeDTO2 = StoreDTO.builder()
                .name("Store XYZ")
                .email("xyz@example.com")
                .phoneNumber("123123123")
                .city(City.builder().build())
                .build();

        boolean equal = storeDTO2.equals(storeDTO);
        boolean hashCodeEqual = storeDTO2.hashCode() == storeDTO.hashCode();

        assertTrue(equal, "StoreDTO should be equal");
        assertTrue(hashCodeEqual, "StoreDTO hashCode should be equal");
    }

}