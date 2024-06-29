package com.myproject.autopartsestoresystem.model;
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
class ModelIdTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<ModelId>> violations;
    private ConstraintViolation<ModelId> violation;
    private ModelId modelId;

    public ModelIdTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        modelId = new ModelId(1L, "330");
    }

    @Test
    void testId_whenValidInputProvided_thenCorrect() {

        //given
        modelId.setId(1L);

        //when
        violations = validator.validate(modelId);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid ID");
    }

    @Test
    void testName_whenValidInputProvided_thenCorrect() {

        //given
        modelId.setName("330");

        //when
        violations = validator.validate(modelId);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid name");
    }

    @Test
    void testName_whenNameIsNull_thenValidationFailed() {

        //given
        modelId.setName(null);

        //when
        violations = validator.validate(modelId);

        //then
        violation = violations.iterator().next();
        assertEquals("Name cannot be blank", violation.getMessage());
    }

    @Test
    void testName_whenNameIsGreaterThan50Characters_thenValidationFailed() {

        //given
        modelId.setName("asdflasdkfhalsdkjfhasdfljkasdhflasdkjfhasdlkjfhasdlfjkhsdlfkahlfjkashkasldfjkhallasdkjf");

        //when
        violations = validator.validate(modelId);

        //then
        violation = violations.iterator().next();
        assertEquals("Name cannot be greater than 50 characters", violation.getMessage());
    }
}