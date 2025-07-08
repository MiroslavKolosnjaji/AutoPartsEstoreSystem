package com.myproject.autopartsestoresystem.models.dto;

import com.myproject.autopartsestoresystem.brands.entity.Brand;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class ModelDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<ModelDTO>> violations;
    private ConstraintViolation<ModelDTO> violation;
    private ModelDTO modelDTO;

    public ModelDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }


    @BeforeEach
    void setUp() {
        modelDTO = ModelDTO.builder()
                .id(1)
                .name("330")
                .brand(new Brand(1, "BMW", null))
                .build();
    }

    @Test
    void testIdValidation_whenValidInputProvided_thenCorrect() {

        //when
        violations = validator.validate(modelDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid id");
    }

    @Test
    void testIdValidation_whenIdIsNull_thenValidationFailed() {

        //given
        modelDTO.setId(null);

        //when
        violations = validator.validate(modelDTO);

        //then
        violation = violations.iterator().next();
        assertEquals("Id is required", violation.getMessage());
    }

    @Test
    void testBrand_whenValidInputProvided_thenCorrect() {

        //when
        violations = validator.validate(modelDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid brand");
    }

    @Test
    void testBrand_whenBrandIsNull_thenValidationFailed() {

        //given
        modelDTO.setBrand(null);

        //when
        violations = validator.validate(modelDTO);

        //then
        violation = violations.iterator().next();
        assertEquals("Brand is required", violation.getMessage());
    }
}