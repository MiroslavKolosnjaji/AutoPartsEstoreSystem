package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.Currency;
import com.myproject.autopartsestoresystem.parts.entity.Price;
import com.myproject.autopartsestoresystem.model.PurchaseOrder;
import com.myproject.autopartsestoresystem.parts.dto.PartDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PurchaseOrderItemDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<PurchaseOrderItemDTO>> violations;
    private ConstraintViolation<PurchaseOrderItemDTO> violation;
    private PurchaseOrderItemDTO purchaseOrderItemDTO;

    public PurchaseOrderItemDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();;
    }

    @BeforeEach
    void setUp() {
        purchaseOrderItemDTO = PurchaseOrderItemDTO.builder()
                .purchaseOrder(PurchaseOrder.builder().build())
                .purchaseOrderId(2L)
                .part(PartDTO.builder()
                        .id(1L)
                        .partNumber("1231231")
                        .prices(List.of(Price.builder().price(new BigDecimal("123.99")).currency(Currency.USD).build()))
                        .build())
                .quantity(3)
                .unitPrice(new BigDecimal("124.99"))
                .build();
    }

    @Test
    void testOrdinalNumber_whenValidInputProvided_thenCorrect() {

        //given
        purchaseOrderItemDTO.setOrdinalNumber(1);

        //when
        violations = validator.validate(purchaseOrderItemDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid ordinal number");
    }

    @Test
    void testOrdinalNumber_whenNegativeNumberProvided_ValidationFailed() {

        //given
        purchaseOrderItemDTO.setOrdinalNumber(-1);

        //when
        violations = validator.validate(purchaseOrderItemDTO);

        //then
        violation = violations.iterator().next();
        assertFalse(violations.isEmpty(),"Violation should fail for negative ordinal number");
        assertTrue(violation.getMessage().contains("Ordinal num starts from 1"));
    }

    @Test
    void testPart_whenValidInputProvided_thenCorrect() {

        //given
        purchaseOrderItemDTO.setPart(PartDTO.builder().id(2L).build());

        //when
        violations = validator.validate(purchaseOrderItemDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid part");
    }

    @Test
    void testPart_whenInputIsNull_thenValidationFailed() {

        //given
        purchaseOrderItemDTO.setPart(null);

        //when
        violations = validator.validate(purchaseOrderItemDTO);

        //then
        violation = violations.iterator().next();
        assertFalse(violations.isEmpty(),"Validation should fail for null input");
        assertTrue(violation.getMessage().contains("Part cannot be null"));
    }

    @Test
    void testQuantity_whenValidInputProvided_thenCorrect() {

        //given
        purchaseOrderItemDTO.setQuantity(3);

        //when
        violations = validator.validate(purchaseOrderItemDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid quantity");
    }

    @Test
    void testQuantity_whenQuantityIsLessThanOne_thenValidationFailed() {

        //given
        purchaseOrderItemDTO.setQuantity(-1);

        //when
        violations = validator.validate(purchaseOrderItemDTO);

        //then
        violation = violations.iterator().next();
        assertFalse(violations.isEmpty(),"Validation should fail for negative quantity");
        assertTrue(violation.getMessage().contains("Quantity cannot be less than 1"));
    }

    @Test
    void testQuantity_whenInputIsNull_thenValidationFailed() {

        //given
        purchaseOrderItemDTO.setQuantity(null);

        //when
        violations = validator.validate(purchaseOrderItemDTO);

        //then
        violation = violations.iterator().next();
        assertFalse(violations.isEmpty(),"Validation should fail for null input");
        assertTrue(violation.getMessage().contains("Quantity cannot be null"));
    }

    @Test
    void testUnitPrice_whenValidInputProvided_thenCorrect() {

        //given
        purchaseOrderItemDTO.setUnitPrice(new BigDecimal("123.99"));

        //when
        violations = validator.validate(purchaseOrderItemDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid unitPrice");
    }

    @Test
    void testUnitPrice_whenInputIsNegativeNumber_thenValidationFailed() {

        //given
        purchaseOrderItemDTO.setUnitPrice(new BigDecimal("-123.99"));

        //when
        violations = validator.validate(purchaseOrderItemDTO);

        //then
        violation = violations.iterator().next();
        assertFalse(violations.isEmpty(),"Validation should fail for negative unitPrice");
        assertTrue(violation.getMessage().contains("Unit price cannot be negative"));
    }

    @Test
    void testTotalPrice_whenValidInputProvided_thenCorrect() {

        //given
        purchaseOrderItemDTO.setTotalPrice(new BigDecimal("123.99"));

        //when
        violations = validator.validate(purchaseOrderItemDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid totalPrice");
    }

    @Test
    void testTotalPrice_whenInputIsNegativeNumber_thenValidationFailed() {

        //given
        purchaseOrderItemDTO.setTotalPrice(new BigDecimal("-123.99"));

        //when
        violations = validator.validate(purchaseOrderItemDTO);

        //then
        violation = violations.iterator().next();
        assertFalse(violations.isEmpty(),"Validation should fail for negative totalPrice");
        assertTrue(violation.getMessage().contains("Total price cannot be negative"));
    }


    @Test
    void testIsEqual_whenCompareTwoObjectsWithSameDetails_thenIsEqual() {

        //given
        PurchaseOrderItemDTO purchaseOrderItemDTO2 = PurchaseOrderItemDTO.builder()
                .purchaseOrder(PurchaseOrder.builder().build())
                .purchaseOrderId(2L)
                .part(PartDTO.builder()
                        .id(1L)
                        .partNumber("1231231")
                        .prices(List.of(Price.builder().price(new BigDecimal("123.99")).currency(Currency.USD).build()))
                        .build())
                .quantity(3)
                .unitPrice(new BigDecimal("124.99"))
                .build();

        boolean isEqual = purchaseOrderItemDTO2.equals(purchaseOrderItemDTO);
        boolean hashCodeIsEqual = purchaseOrderItemDTO2.hashCode() == purchaseOrderItemDTO.hashCode();

        //when && then
        assertEquals(purchaseOrderItemDTO2, purchaseOrderItemDTO);

        assertTrue(isEqual, "PurchaseOrderDTO2 should be equal to PurchaseOrderDTO");
        assertTrue(hashCodeIsEqual, "PurchaseOrderDTO2 hashCode should be equal to PurchaseOrderDTO hashCode");

    }
}