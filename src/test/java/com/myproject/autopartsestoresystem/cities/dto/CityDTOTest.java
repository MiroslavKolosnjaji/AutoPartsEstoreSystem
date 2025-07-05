package com.myproject.autopartsestoresystem.cities.dto;

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
class CityDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<CityDTO>> violations;
    private ConstraintViolation<CityDTO> violation;
    private CityDTO cityDTO;

    public CityDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        cityDTO = CityDTO.builder()
                .id(1L)
                .name("Berlin")
                .zipCode("12345")
                .build();
    }

    @DisplayName("Name - valid input")
    @Test
    void testNameValidation_whenValidInputProvided_thenCorrect() {

        //given
        cityDTO.setName("Paris");

        //when
        violations = validator.validate(cityDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid name");
    }

    @DisplayName("Name - null - invalid input")
    @Test
    void testNameValidation_whenInputIsNull_thenValidationFailed() {

        //given
        cityDTO.setName(null);

        //when
        violations = validator.validate(cityDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for name that is null");

        violation = violations.iterator().next();
        assertEquals("City name cannot be blank", violation.getMessage());
    }

    @DisplayName("ZipCode - Valid Input")
    @Test
    void testZipCodeValidation_whenValidInputProvided_thenCorrect() {

        //given
        cityDTO.setZipCode("12345");

        //when
        violations = validator.validate(cityDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid zip code");
    }

    @DisplayName("ZipCode - Empty String - Invalid Input")
    @Test
    void testZipCodeValidation_whenInvalidInputProvided_thenValidationFailed() {

        //given
        cityDTO.setZipCode("");

        //when
        violations = validator.validate(cityDTO);

        //then
        violation = violations.iterator().next();
        assertEquals("Zip code must contain 5 digits", violation.getMessage());
    }

    @DisplayName("ZipCode - Input Shorter Than 5 Digits - Invalid Input")
    @Test
    void testZipCodeValidation_whenInputIsShorterThan5Digits_thenValidationFailed() {

        //given
        cityDTO.setZipCode("1234");

        //when
        violations = validator.validate(cityDTO);

        //then
        violation = violations.iterator().next();
        assertEquals("Zip code must contain 5 digits", violation.getMessage());
    }

    @Test
    void testIsEqual_whenCompareTwoObjectsWithSameDetails_thenIsEqual() {

        //given
        CityDTO cityDTO2 = CityDTO.builder()
                .id(1L)
                .name("Berlin")
                .zipCode("12345")
                .build();

        //when
        boolean isEqual = cityDTO2.equals(cityDTO);


        //then
        assertTrue(isEqual, "City should be equal");
    }

    @Test
    void testIsEqual_whenCOmpareTwoObjectsWithSameDetails_thenEqual() {

        //given
        CityDTO cityDTO2 = CityDTO.builder()
                .id(1L)
                .name("Berlin")
                .zipCode("12345")
                .build();

        //when
        boolean isEqual = cityDTO2.equals(cityDTO);
        boolean isEqualHashCode = cityDTO2.hashCode() == cityDTO.hashCode();

        //then
        assertTrue(isEqual, "CityDTO should be equal");
        assertTrue(isEqualHashCode, "CityDTO hashCode should be equal");
    }
}