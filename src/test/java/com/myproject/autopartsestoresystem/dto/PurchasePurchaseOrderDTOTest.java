package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.PurchaseOrderStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PurchasePurchaseOrderDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<PurchaseOrderDTO>> violations;
    private ConstraintViolation<PurchaseOrderDTO> violation;
    private PurchaseOrderDTO purchaseOrderDTO;

    public PurchasePurchaseOrderDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        purchaseOrderDTO = PurchaseOrderDTO.builder()
                .id(1L)
                .status(PurchaseOrderStatus.COMPLETED)
                .purchaseOrderNumber(UUID.randomUUID())
                .items(new ArrayList<>())
                .customer(CustomerDTO.builder().build())
                .totalAmount(new BigDecimal("100.00"))
                .build();
    }

    @Test
    void testCartNumberValidation_whenValidDetailsProvided_thenCorrect() {

        //given
        purchaseOrderDTO.setPurchaseOrderNumber(UUID.randomUUID());

        //when
        violations = validator.validate(purchaseOrderDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid cart number");
    }

    @Test
    void testCartNumberValidation_whenCartNumberIsNull_thenValidationFailed() {

        //given
        purchaseOrderDTO.setPurchaseOrderNumber(null);

        //when
        violations = validator.validate(purchaseOrderDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid cart number");

        violation = violations.iterator().next();
        assertEquals("Cart number cannot be null", violation.getMessage());
    }

    @Test
    void testStatusValidation_whenValidStatusProvided_thenCorrect() {

        //given
        purchaseOrderDTO.setStatus(PurchaseOrderStatus.CANCELLED);

        //when
        violations = validator.validate(purchaseOrderDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid cart status");
    }

    @Test
    void testStatusValidation_whenInvalidStatusProvided_thenValidationFailed() {

        //given
        purchaseOrderDTO.setStatus(null);

        //when
        violations = validator.validate(purchaseOrderDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid cart status");

        violation = violations.iterator().next();
        assertEquals("Status cannot be null", violation.getMessage());
    }

    @Test
    void testTotalAmountValidation_whenValidDetailsProvided_thenCorrect() {

        //given
        purchaseOrderDTO.setTotalAmount(new BigDecimal("100.00"));

        //when
        violations = validator.validate(purchaseOrderDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid total amount");
    }

    @Test
    void testTotalAmountValidation_whenNegativeNumberProvided_thenValidationFailed() {

        //given
        purchaseOrderDTO.setTotalAmount(new BigDecimal("-100.00"));

        //when
        violations = validator.validate(purchaseOrderDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid total amount");

        violation = violations.iterator().next();
        assertEquals("Total amount cannot be negative", violation.getMessage());
    }

    @Test
    void testTotalAmountValidation_whenTotalAmountIsNull_thenValidationFailed() {

        //given
        purchaseOrderDTO.setTotalAmount(null);

        //when
        violations = validator.validate(purchaseOrderDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for total amount that is NULL");

        violation = violations.iterator().next();
        assertEquals("Total amount cannot be null", violation.getMessage());
    }

    @Test
    void testItemsValidation_whenValidDetailsProvided_thenCorrect() {

        //given
        purchaseOrderDTO.setItems(new ArrayList<>());

        //when
        violations = validator.validate(purchaseOrderDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid items");
    }

    @Test
    void testItemsValidation_whenListIsNull_thenValidationFailed() {

        //given
        purchaseOrderDTO.setItems(null);

        //when
        violations = validator.validate(purchaseOrderDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid items");

        violation = violations.iterator().next();
        assertEquals("Item list cannot be null", violation.getMessage());
    }

    @Test
    void testIsEqual_whenCompareTwoObjectsWIthSameDetails_thenIsEqual() {

        //given
        PurchaseOrderDTO purchaseOrderDTO2 = PurchaseOrderDTO.builder()
                .id(1L)
                .status(PurchaseOrderStatus.COMPLETED)
                .purchaseOrderNumber(UUID.randomUUID())
                .items(new ArrayList<>())
                .customer(CustomerDTO.builder().build())
                .totalAmount(new BigDecimal("100.00"))
                .build();

        boolean isEqual = purchaseOrderDTO2.equals(purchaseOrderDTO);
        boolean isEqualHashCode = purchaseOrderDTO2.hashCode() == purchaseOrderDTO.hashCode();

        assertTrue(isEqual, "OrderDTO should be equal");
        assertTrue(isEqualHashCode, "OrderDTO hashCode should be equal");
    }
}