package com.myproject.autopartsestoresystem.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PurchaseOrderTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {


        //given
        UUID orderNumber = UUID.randomUUID();

        PurchaseOrder purchaseOrder1 = PurchaseOrder.builder()
                .id(1L)
                .purchaseOrderNumber(orderNumber)
                .status(PurchaseOrderStatus.PROCESSING)
                .totalAmount(new BigDecimal("100.00"))
                .purchaseOrderItems(null)
                .customer(null)
                .build();

        PurchaseOrder purchaseOrder2 = PurchaseOrder.builder()
                .id(1L)
                .purchaseOrderNumber(orderNumber)
                .status(PurchaseOrderStatus.PROCESSING)
                .totalAmount(new BigDecimal("100.00"))
                .purchaseOrderItems(null)
                .customer(null)
                .build();

        boolean equals = purchaseOrder1.equals(purchaseOrder2);
        boolean hashCode = purchaseOrder1.hashCode() == purchaseOrder2.hashCode();

        //when && then
        assertAll("Cart fields validation",
                () -> assertEquals(purchaseOrder1.getId(), purchaseOrder2.getId(), "Order ID mismatch"),
                () -> assertEquals(purchaseOrder1.getPurchaseOrderNumber(), purchaseOrder2.getPurchaseOrderNumber(), "Order number mismatch"),
                () -> assertEquals(purchaseOrder1.getStatus(), purchaseOrder2.getStatus(), "Order status mismatch"),
                () -> assertEquals(purchaseOrder1.getTotalAmount(), purchaseOrder2.getTotalAmount(), "Order total amount mismatch"),
                () -> assertEquals(purchaseOrder1.getPurchaseOrderItems(), purchaseOrder2.getPurchaseOrderItems(), "Order items mismatch"),
                () -> assertEquals(purchaseOrder1.getCustomer(), purchaseOrder2.getCustomer(), "Order customer mismatch"));

        assertTrue(equals,"Order is not equal");
        assertTrue(hashCode,"Order hash code not equal");
    }
}