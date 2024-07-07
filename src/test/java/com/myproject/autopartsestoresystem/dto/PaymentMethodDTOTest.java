package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.PaymentType;
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
class PaymentMethodDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<PaymentMethodDTO>> violations;
    private ConstraintViolation<PaymentMethodDTO> violation;
    private PaymentMethodDTO paymentMethodDTO;

    public PaymentMethodDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        paymentMethodDTO = PaymentMethodDTO.builder().id(1L).paymentType(PaymentType.DEBIT_CARD).build();
    }

    @Test
    void testPaymentTypeValidation_whenValidInputProvided_thenCorrect() {

        //given
        paymentMethodDTO.setPaymentType(PaymentType.CREDIT_CARD);

        //when
        violations = validator.validate(paymentMethodDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid payment type");
    }

    @Test
    void testPaymentTypeValidation_whenInvalidInputProvided_thenValidationFailed() {

        //given
        paymentMethodDTO.setPaymentType(null);

        //when
        violations = validator.validate(paymentMethodDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for payment type that is null");

        violation = violations.iterator().next();
        assertEquals("Payment type cannot be null", violation.getMessage());
    }
}