package com.myproject.autopartsestoresystem.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PurchaseOrderItemTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {

        //given
        BigDecimal price = new BigDecimal("12.5");
        PurchaseOrderItem purchaseOrderItem1 = PurchaseOrderItem.builder()
                .id(new PurchaseOrderItemId(1L, 1))
                .quantity(1)
                .unitPrice(price)
                .totalPrice(price)
                .build();

        PurchaseOrderItem purchaseOrderItem2 = PurchaseOrderItem.builder()
                .id(new PurchaseOrderItemId(1L, 1))
                .quantity(1)
                .unitPrice(price)
                .totalPrice(price)
                .build();

        boolean equal = purchaseOrderItem1.equals(purchaseOrderItem2);
        boolean hashcode = purchaseOrderItem1.hashCode() == purchaseOrderItem2.hashCode();

        //when && then
        assertAll("Item fields validation",
        () -> assertEquals(purchaseOrderItem1.getId(), purchaseOrderItem2.getId(), "Item ID mismatch"),
        () -> assertEquals(purchaseOrderItem1.getQuantity(), purchaseOrderItem2.getQuantity(), "Item quantity mismatch"),
        () -> assertEquals(purchaseOrderItem1.getUnitPrice(), purchaseOrderItem2.getUnitPrice(), "Item quantity mismatch"),
        () -> assertEquals(purchaseOrderItem1.getUnitPrice(), purchaseOrderItem2.getUnitPrice(), "Item unit price mismatch"),
        () -> assertEquals(purchaseOrderItem1.getTotalPrice(), purchaseOrderItem2.getTotalPrice(), "Item total price mismatch"));

        assertTrue(equal, "Item are not equal");
        assertTrue(hashcode, "Item hash code are not equal");

    }
}