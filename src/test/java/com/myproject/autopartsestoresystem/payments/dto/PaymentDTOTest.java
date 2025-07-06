package com.myproject.autopartsestoresystem.payments.dto;

import com.myproject.autopartsestoresystem.payments.entity.PaymentMethod;
import com.myproject.autopartsestoresystem.payments.entity.PaymentStatus;
import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrder;
import com.myproject.autopartsestoresystem.parts.dto.PaymentDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PaymentDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<PaymentDTO>> violations;
    private ConstraintViolation<PaymentDTO> violation;
    private PaymentDTO paymentDTO;

    public PaymentDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        paymentDTO = PaymentDTO.builder()
                .id(1L)
                .paymentMethod(PaymentMethod.builder().build())
                .purchaseOrder(PurchaseOrder.builder().build())
                .status(PaymentStatus.PROCESSING)
                .amount(new BigDecimal("123.00"))
                .build();
    }


    @DisplayName("PaymentMethod - Valid Input")
    @Test
    void testPaymentMethod_whenValidInputProvided_thenCorrect() {

        //given
        paymentDTO.setPaymentMethod(PaymentMethod.builder().build());

        //when
        violations = validator.validate(paymentDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid PaymentMethod");
    }

    @DisplayName("PaymentMethod - Invalid Input")
    @Test
    void testPaymentMethod_whenInputIsNull_thenValidationFailed() {

        //given
        paymentDTO.setPaymentMethod(null);

        //when
        violations = validator.validate(paymentDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for PaymentMethod");
        violation = violations.iterator().next();
        assertEquals("Payment method cannot be null", violation.getMessage());
    }

    @DisplayName("PurchaseOrder - Valid Input")
    @Test
    void testPurchaseOrder_whenValidInputProvided_thenCorrect() {

        //given
        paymentDTO.setPurchaseOrder(PurchaseOrder.builder().build());

        //when
        violations = validator.validate(paymentDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid PurchaseOrder");
    }

    @DisplayName("PurchaseOrder - Invalid Input")
    @Test
    void testPurchaseOrder_whenInputIsNull_thenValidationFailed() {

        //given
        paymentDTO.setPurchaseOrder(null);

        //when
        violations = validator.validate(paymentDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for PurchaseOrder");
        violation = violations.iterator().next();
        assertEquals("Purchase Order cannot be null", violation.getMessage());
    }

    @DisplayName("PaymentStatus - Valid Input")
    @Test
    void testPaymentStatus_whenValidInputProvided_thenCorrect() {

        //given
        paymentDTO.setStatus(PaymentStatus.PROCESSING);

        //when
        violations = validator.validate(paymentDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid Status");
    }

    @DisplayName("PaymentStatus - Invalid Input")
    @Test
    void testPaymentStatus_whenInputIsNull_thenValidationFailed() {

        //given
        paymentDTO.setStatus(null);

        //when
        violations = validator.validate(paymentDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for PaymentStatus");
        violation = violations.iterator().next();
        assertEquals("Payment status cannot be null", violation.getMessage());
    }

    @DisplayName("Amount - Valid Input")
    @Test
    void testAmount_whenValidInputProvided_thenCorrect() {

        //given
        paymentDTO.setAmount(new BigDecimal("123.00"));

        //when
        violations = validator.validate(paymentDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid Amount");
    }

    @DisplayName("Amount - Negative Values")
    @Test
    void testAmount_whenNegativeValueIsProvided_thenValidationFailed() {

        //given
        paymentDTO.setAmount(new BigDecimal("-123.00"));

        //when
        violations = validator.validate(paymentDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for negative Value");
        violation = violations.iterator().next();
        assertEquals("Amount cannot be negative", violation.getMessage());
    }

    @Test
    void testIsEqual_whenCompareTwoObjectsWithSameDetails_thenIsEqual() {

        PaymentDTO paymentDTO2 = PaymentDTO.builder()
                .id(1L)
                .paymentMethod(PaymentMethod.builder().build())
                .purchaseOrder(PurchaseOrder.builder().build())
                .status(PaymentStatus.PROCESSING)
                .amount(new BigDecimal("123.00"))
                .build();

        boolean equal = paymentDTO2.equals(paymentDTO);
        boolean hashCodeEqual = paymentDTO2.hashCode() == paymentDTO.hashCode();

        assertTrue(equal, "PaymentDTO should be equal");
        assertTrue(hashCodeEqual, "PaymentDTO hashCode should be equal");
    }
}