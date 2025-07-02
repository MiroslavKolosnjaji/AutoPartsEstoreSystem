package com.myproject.autopartsestoresystem.parts.entity;

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

    @Test
    void testIsEqual_whenCompareTwoObjectsWithSameDetails_thenIsEqual() {

        //given
        PriceId priceId2 = new PriceId(1L, 0L);

        //given
        boolean isEqual = priceId2.equals(priceId);
        boolean isEqualHashCode = priceId2.hashCode() == priceId.hashCode();

        //then
        assertTrue(isEqual, "PriceId should be equal");
        assertTrue(isEqualHashCode, "PriceId hashCode should be equal");
    }
}