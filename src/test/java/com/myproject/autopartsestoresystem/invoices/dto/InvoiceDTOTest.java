package com.myproject.autopartsestoresystem.invoices.dto;

import com.myproject.autopartsestoresystem.invoices.entity.InvoiceItem;
import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrder;
import com.myproject.autopartsestoresystem.model.Store;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class InvoiceDTOTest {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private Set<ConstraintViolation<InvoiceDTO>> violations;
    private ConstraintViolation<InvoiceDTO> violation;
    private InvoiceDTO invoiceDTO;

    public InvoiceDTOTest() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    @BeforeEach
    void setUp() {

        List<InvoiceItem> invoiceItems = new LinkedList<>();
        invoiceItems.add(InvoiceItem.builder().build());

        invoiceDTO = InvoiceDTO.builder()
                .store(Store.builder().build())
                .purchaseOrder(PurchaseOrder.builder().build())
                .invoiceItems(invoiceItems)
                .totalAmount(new BigDecimal("99.99"))
                .build();
    }

    @DisplayName("Store - Valid Input")
    @Test
    void testStoreValidation_whenValidInputProvided_thenCorrect() {

        //given
        invoiceDTO.setStore(Store.builder().build());

        //when
        violations = validator.validate(invoiceDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid store");
    }

    @DisplayName("Store - Null - Invalid Input")
    @Test
    void testStore_whenNullInputProvided_thenValidationFailed() {

        //given
        invoiceDTO.setStore(null);

        //when
        violations = validator.validate(invoiceDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for store that is null");

        violation = violations.iterator().next();
        assertEquals("Store is required", violation.getMessage());
    }

    @DisplayName("Purchase Order - Valid Input")
    @Test
    void testPurchaseOrder_whenValidInputProvided_thenCorrect() {

        //given
        invoiceDTO.setPurchaseOrder(PurchaseOrder.builder().build());

        //when
        violations = validator.validate(invoiceDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid purchase order");
    }

    @DisplayName("Purchase Order - Null - Invalid Input")
    @Test
    void testPurchaseOrder_whenNullInputProvided_thenValidationFailed() {

        //given
        invoiceDTO.setPurchaseOrder(null);

        //when
        violations = validator.validate(invoiceDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for purchase order");

        violation = violations.iterator().next();
        assertEquals("Purchase Order is required", violation.getMessage());
    }

    @DisplayName("Invoice Items - Valid Input")
    @Test
    void testInvoiceItems_whenValidInputProvided_thenCorrect() {

        //given
        invoiceDTO.setInvoiceItems(List.of(InvoiceItem.builder().build()));

        //when
        violations = validator.validate(invoiceDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid invoice items");
    }

    @DisplayName("Invoice Items Empty List - Invalid Input")
    @Test
    void testInvoiceItems_whenEmptyListProvided_thenValidationFailed() {

        //given
        invoiceDTO.setInvoiceItems(List.of());

        //when
        violations = validator.validate(invoiceDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for empty invoice items list");

        violation = violations.iterator().next();
        assertEquals("Invoice Items are required", violation.getMessage());
    }

    @DisplayName("Invoice Items - Null - Invalid Input")
    @Test
    void testInvoiceItems_whenNullInputProvided_thenValidationFailed() {

        //given
        invoiceDTO.setInvoiceItems(null);

        //when
        violations = validator.validate(invoiceDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for invoice items list that is null");

        violation = violations.iterator().next();
        assertEquals("Invoice Items are required", violation.getMessage());
    }

    @DisplayName("Total Amount - Valid Input")
    @Test
    void testTotalAmount_whenValidInputProvided_thenCorrect() {

        //given
        invoiceDTO.setTotalAmount(new BigDecimal("99.99"));

        //when
        violations = validator.validate(invoiceDTO);

        //then
        assertTrue(violations.isEmpty(), "Violation should pass for valid total amount");
    }

    @DisplayName("Total Amount - Invalid Input")
    @Test
    void testTotalAmount_whenInvalidInputProvided_thenValidationFailed() {

        //given
        invoiceDTO.setTotalAmount(new BigDecimal("-1.99"));

        //when
        violations = validator.validate(invoiceDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for total amount");

        violation = violations.iterator().next();
        assertEquals("Total Amount must be zero or above", violation.getMessage());
    }

    @DisplayName("Total Amount - Null - Invalid Input")
    @Test
    void testTotalAmount_whenNullInputProvided_thenValidationFailed() {

        //given
        invoiceDTO.setTotalAmount(null);

        //when
        violations = validator.validate(invoiceDTO);

        //then
        assertFalse(violations.isEmpty(), "Violation should fail for total amount");

        violation = violations.iterator().next();
        assertEquals("Total Amount required", violation.getMessage());
    }

    @DisplayName("Test Equality Of Two InvoiceDTO Objects")
    @Test
    void testIsEqual_whenCOmpareTwoObjectsWithSameDetails_thenEqual() {

        //given
        InvoiceDTO invoiceDTO2 = InvoiceDTO.builder()
                .store(Store.builder().build())
                .purchaseOrder(PurchaseOrder.builder().build())
                .invoiceItems(invoiceDTO.getInvoiceItems())
                .totalAmount(new BigDecimal("99.99"))
                .build();

        //when
        boolean isEqual = invoiceDTO2.equals(invoiceDTO);
        boolean isEqualHashCode = invoiceDTO2.hashCode() == invoiceDTO.hashCode();

        //then
        assertTrue(isEqual, "InvoiceDTO should be equal");
        assertTrue(isEqualHashCode, "InvoiceDTO hashcode should be equal");
    }
}