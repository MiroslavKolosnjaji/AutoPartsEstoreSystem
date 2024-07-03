package com.myproject.autopartsestoresystem.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PriceIdTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<PriceId>> violations;
    private ConstraintViolation<PriceId> violation;
    private PriceId priceId;

    public PriceIdTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        priceId = new PriceId(1L, 0L);
    }

    @Test
    void testPartId_whenValidInputProvided_thenCorrect() {

        //given
        priceId.setPartId(1L);

        //when
        violations = validator.validate(priceId);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid part ID");
    }
}