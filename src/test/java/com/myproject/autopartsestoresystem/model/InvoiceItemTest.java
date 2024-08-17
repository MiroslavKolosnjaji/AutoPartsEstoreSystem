package com.myproject.autopartsestoresystem.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class InvoiceItemTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {

        //given
        InvoiceItem invoiceItem1 = InvoiceItem.builder()
                .invoice(Invoice.builder().build())
                .productName("Test Product")
                .unitPrice(BigDecimal.TEN)
                .totalPrice(BigDecimal.TEN)
                .quantity(1)
                .discountPercentage(BigDecimal.ZERO)
                .taxPercentage(BigDecimal.ZERO)
                .build();

        InvoiceItem invoiceItem2 = InvoiceItem.builder()
                .invoice(Invoice.builder().build())
                .productName("Test Product")
                .unitPrice(BigDecimal.TEN)
                .totalPrice(BigDecimal.TEN)
                .quantity(1)
                .discountPercentage(BigDecimal.ZERO)
                .taxPercentage(BigDecimal.ZERO)
                .build();

        boolean equals = invoiceItem1.equals(invoiceItem2);
        boolean hashCodeEquals = invoiceItem1.hashCode() == invoiceItem2.hashCode();

        assertAll("Invoice Item Fields Validation",
                () -> assertEquals(invoiceItem1.getId(), invoiceItem2.getId(), "InvoiceItem ID mismatch"),
                () -> assertEquals(invoiceItem1.getInvoice(), invoiceItem2.getInvoice(), "InvoiceItem Invoice mismatch"),
                () -> assertEquals(invoiceItem1.getQuantity(), invoiceItem2.getQuantity(), "InvoiceItem Quantity mismatch"),
                () -> assertEquals(invoiceItem1.getTotalPrice(), invoiceItem2.getTotalPrice(), "InvoiceItem TotalPrice mismatch"),
                () -> assertEquals(invoiceItem1.getProductName(), invoiceItem2.getProductName(), "InvoiceItem ProductName mismatch"),
                () -> assertEquals(invoiceItem1.getUnitPrice(), invoiceItem2.getUnitPrice(), "InvoiceItem UnitPrice mismatch"),
                () -> assertEquals(invoiceItem1.getDiscountPercentage(), invoiceItem2.getDiscountPercentage(), "InvoiceItem DiscountPercentage mismatch"),
                () -> assertEquals(invoiceItem1.getTaxPercentage(), invoiceItem2.getTaxPercentage(), "InvoiceItem TaxPercentage mismatch"));

        assertTrue(equals, "InvoiceItem should be equal");
        assertTrue(hashCodeEquals, "InvoiceItem hashCode mismatch");
    }
}