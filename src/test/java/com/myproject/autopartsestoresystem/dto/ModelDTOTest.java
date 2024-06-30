package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.Brand;
import com.myproject.autopartsestoresystem.model.ModelId;
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
                .id(new ModelId(1L, "330"))
                .brand(new Brand(1L, "BMW", null))
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
    void testIdValidation_whenIdisNull_thenValidationFailed() {

        //given
        modelDTO.setId(null);

        //when
        violations = validator.validate(modelDTO);

        //then
        violation = violations.iterator().next();
        assertEquals("Model id can't be null", violation.getMessage());
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
        assertEquals("Brand can't be null", violation.getMessage());
    }
}