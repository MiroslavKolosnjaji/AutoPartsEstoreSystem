package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.Invoice;
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
class InvoiceItemDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<InvoiceItemDTO>> violations;
    private ConstraintViolation<InvoiceItemDTO> violation;
    private InvoiceItemDTO invoiceItemDTO;

    public InvoiceItemDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {
        invoiceItemDTO = InvoiceItemDTO.builder()
                .invoice(Invoice.builder().build())
                .productName("Test Product")
                .unitPrice(BigDecimal.TEN)
                .totalPrice(BigDecimal.TEN)
                .quantity(1)
                .discountPercentage(BigDecimal.ZERO)
                .taxPercentage(BigDecimal.ZERO)
                .build();
    }

    @DisplayName("Product Name - Valid Input")
    @Test
    void testProductName_whenValidInputProvided_thenCorrect() {

        //given
        invoiceItemDTO.setProductName("Test Product");

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid name");
    }

    @DisplayName("Product Name - Blank - Invalid Input")
    @Test
    void testProductName_whenBlankInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setProductName(" ");

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for blank input");

        violation = violations.iterator().next();
        assertEquals("Product name required", violation.getMessage());
    }

    @DisplayName("Product Name - Empty - Invalid Input")
    @Test
    void testProductName_whenEmptyInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setProductName("");

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for empty input");

        violation = violations.iterator().next();
        assertEquals("Product name required", violation.getMessage());
    }

    @DisplayName("Product Name - Null - Invalid Input")
    @Test
    void testProductName_whenNullInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setProductName(null);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for null input");

        violation = violations.iterator().next();
        assertEquals("Product name required", violation.getMessage());
    }

    @DisplayName("Quantity - Valid Input")
    @Test
    void testQuantity_whenValidInputProvided_thenCorrect() {

        //given
        invoiceItemDTO.setQuantity(1);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid quantity");
    }

    @DisplayName("Quantity - Invalid Input")
    @Test
    void testQuantity_whenInvalidInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setQuantity(-1);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid quantity");

        violation = violations.iterator().next();
        assertEquals("Quantity must be greater than zero", violation.getMessage());
    }

    @DisplayName("Quantity - Null - Invalid Input")
    @Test
    void testQuantity_whenNullInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setQuantity(null);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for null input");

        violation = violations.iterator().next();
        assertEquals("Quantity required", violation.getMessage());
    }

    @DisplayName("Unit Price - Valid Input")
    @Test
    void testUnitPrice_whenValidInputProvided_thenCorrect() {

        //given
        invoiceItemDTO.setUnitPrice(BigDecimal.TEN);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid unitPrice");
    }

    @DisplayName("Unit Price - Invalid Input")
    @Test
    void testUnitPrice_whenInvalidInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setUnitPrice(new BigDecimal("-1.00"));

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid unitPrice");

        violation = violations.iterator().next();
        assertEquals("Unit price must be zero or above", violation.getMessage());
    }

    @DisplayName("Unit Price - Null - Invalid Input")
    @Test
    void testUnitPrice_whenNullInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setUnitPrice(null);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for null input");

        violation = violations.iterator().next();
        assertEquals("Unit price required", violation.getMessage());
    }

    @DisplayName("Total Price - Valid Input")
    @Test
    void testTotalPrice_whenValidInputProvided_thenCorrect() {

        //given
        invoiceItemDTO.setTotalPrice(BigDecimal.TEN);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid totalPrice");
    }

    @DisplayName("Total Price - Invalid Input")
    @Test
    void testTotalPrice_whenInvalidInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setTotalPrice(new BigDecimal("-1.00"));

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid totalPrice");

        violation = violations.iterator().next();
        assertEquals("Total price must be zero or above", violation.getMessage());
    }

    @DisplayName("Total Price - Null - Invalid Input")
    @Test
    void testTotalPrice_whenNullInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setTotalPrice(null);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for null input");

        violation = violations.iterator().next();
        assertEquals("Total price required", violation.getMessage());
    }

    @DisplayName("Discount Percentage - Valid Input")
    @Test
    void testDiscountPercentage_whenValidInputProvided_thenCorrect() {

        //given
        invoiceItemDTO.setDiscountPercentage(BigDecimal.TEN);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid discount percentage");
    }

    @DisplayName("Discount Percentage - Below Zero - Invalid Input")
    @Test
    void testDiscountPercentage_whenBelowZeroInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setDiscountPercentage(new BigDecimal("-1.00"));

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid discount percentage");

        violation = violations.iterator().next();
        assertEquals("Discount must be at least 0", violation.getMessage());
    }

    @DisplayName("Discount Percentage - Over 100 - Invalid Input")
    @Test
    void testDiscountPercentage_whenOver100InputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setDiscountPercentage(new BigDecimal("100.01"));

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid discount percentage");

        violation = violations.iterator().next();
        assertEquals("Discount must be at most 100", violation.getMessage());
    }

    @DisplayName("Discount Percentage - Null - Invalid Input")
    @Test
    void testDiscountPercentage_whenNullInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setDiscountPercentage(null);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid discount percentage");

        violation = violations.iterator().next();
        assertEquals("Discount percentage required", violation.getMessage());
    }

    @DisplayName("Tax Percentage - Valid Input")
    @Test
    void testTaxPercentage_whenValidInputProvided_thenCorrect() {

        //given
        invoiceItemDTO.setTaxPercentage(BigDecimal.TEN);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid tax percentage");
    }

    @DisplayName("Tax Percentage - Below Zero - Invalid Input")
    @Test
    void testTaxPercentage_whenBelowZeroInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setTaxPercentage(new BigDecimal("-1.00"));

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid tax percentage");

        violation = violations.iterator().next();
        assertEquals("Tax must be at least 0", violation.getMessage());
    }

    @DisplayName("Tax Percentage - Over 100 - Invalid Input")
    @Test
    void testTaxPercentage_whenOver100InputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setTaxPercentage(new BigDecimal("100.01"));

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid tax percentage");

        violation = violations.iterator().next();
        assertEquals("Tax must be at most 100", violation.getMessage());
    }

    @DisplayName("Tax Percentage - Null - Invalid Input")
    @Test
    void testTaxPercentage_whenNullInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setTaxPercentage(null);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid tax percentage");

        violation = violations.iterator().next();
        assertEquals("Tax percentage required", violation.getMessage());
    }

    @DisplayName("Invoice - Valid Input")
    @Test
    void testInvoice_whenValidInputProvided_thenCorrect() {

        //given
        invoiceItemDTO.setInvoice(Invoice.builder().build());

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertTrue(violations.isEmpty(), "Validation should pass for valid invoice");
    }

    @DisplayName("Invoice - Null - Invalid Input")
    @Test
    void testInvoice_whenNullInputProvided_thenValidationFailed() {

        //given
        invoiceItemDTO.setInvoice(null);

        //when
        violations = validator.validate(invoiceItemDTO);

        //then
        assertFalse(violations.isEmpty(), "Validation should fail for invalid invoice");

        violation = violations.iterator().next();
        assertEquals("Invoice is required", violation.getMessage());
    }

    @DisplayName("Test Equality Of Two InvoiceItemDTO Objects")
    @Test
    void testIsEqual_whenCOmpareTwoObjectsWithSameDetails_thenEqual() {

        //given
        InvoiceItemDTO invoiceItemDTO2 = InvoiceItemDTO.builder()
                .invoice(Invoice.builder().build())
                .productName("Test Product")
                .unitPrice(BigDecimal.TEN)
                .totalPrice(BigDecimal.TEN)
                .quantity(1)
                .discountPercentage(BigDecimal.ZERO)
                .taxPercentage(BigDecimal.ZERO)
                .build();

        //when
        boolean isEqual = invoiceItemDTO2.equals(invoiceItemDTO);
        boolean isEqualHashCode = invoiceItemDTO2.hashCode() == invoiceItemDTO.hashCode();

        //then
        assertTrue(isEqual, "InvoiceItemDTO should be equal");
        assertTrue(isEqualHashCode, "InvoiceItemDTO hashCode should be equal");
    }
}