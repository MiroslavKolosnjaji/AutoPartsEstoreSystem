package com.myproject.autopartsestoresystem.model;

import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class InvoiceTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {

        //given
        Invoice invoice1 = Invoice.builder()
                .store(Store.builder().build())
                .purchaseOrder(PurchaseOrder.builder().build())
                .invoiceItems(new LinkedList<>())
                .totalAmount(new BigDecimal("99.99"))
                .build();

        Invoice invoice2 = Invoice.builder()
                .store(Store.builder().build())
                .purchaseOrder(PurchaseOrder.builder().build())
                .invoiceItems(new LinkedList<>())
                .totalAmount(new BigDecimal("99.99"))
                .build();

        //when
        boolean isEqual = invoice1.equals(invoice2);
        boolean hashCodeIsEqual = invoice1.hashCode() == invoice2.hashCode();

        assertAll("Invoice Field Validation",
                () -> assertEquals(invoice1.getId(), invoice2.getId(), "Invoice ID mismatch"),
                () -> assertEquals(invoice1.getInvoiceItems(), invoice2.getInvoiceItems(), "Invoice InvoiceItems mismatch"),
                () -> assertEquals(invoice1.getTotalAmount(), invoice2.getTotalAmount(), "Invoice TotalAmount mismatch"),
                () -> assertEquals(invoice1.getStore(), invoice2.getStore(), "Invoice Store mismatch"),
                () -> assertEquals(invoice1.getPurchaseOrder(), invoice2.getPurchaseOrder(), "Invoice PurchaseOrder mismatch"));

        assertTrue(isEqual, "Invoice should be equal");
        assertTrue(hashCodeIsEqual, "Invoice hashCode mismatch");
    }
}