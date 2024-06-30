package com.myproject.autopartsestoresystem.dto;

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
class BrandDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<BrandDTO>> violations;
    private ConstraintViolation<BrandDTO> violation;
    private BrandDTO brandDTO;

    public BrandDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        brandDTO = BrandDTO.builder().id(1L).name("BMW").build();
    }

    @DisplayName("Name - valid input")
    @Test
    void testNameValidation_whenValidInputProvided_thenCorrect() {

        //given
        brandDTO.setName("Audi");

        //when
        violations = validator.validate(brandDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should be empty");
    }

    @DisplayName("Name - null - invalid input")
    @Test
    void testNameValidation_whenInputIsNull_thenValidationFailed() {

        //given
        brandDTO.setName(null);

        //when
        violations = validator.validate(brandDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should be empty");

        violation = violations.iterator().next();
        assertEquals("Brand name cannot be blank", violation.getMessage(), "Message is incorrect");
    }

    @Test
    void testIsEqual_whenCompareTwoObjectsWithSameDetails_thenIsEqual() {

        //given
        BrandDTO brandDTO2 = BrandDTO.builder().id(1L).name("BMW").build();

        //when
        boolean isEqual = brandDTO2.equals(brandDTO);

        //then
        assertTrue(isEqual, "Brand should be equal");
    }
}