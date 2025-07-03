package com.myproject.autopartsestoresystem.orders.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PurchaseOrderItemIdTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {

        //given
        PurchaseOrderItemId purchaseOrderItemId1 = PurchaseOrderItemId.builder()
                .purchaseOrderId(1L)
                .ordinalNumber(1)
                .build();

        PurchaseOrderItemId purchaseOrderItemId2 = PurchaseOrderItemId.builder()
                .purchaseOrderId(1L)
                .ordinalNumber(1)
                .build();

        boolean isequal = purchaseOrderItemId1.equals(purchaseOrderItemId2);
        boolean hasCode = purchaseOrderItemId1.hashCode() == purchaseOrderItemId2.hashCode();

        PurchaseOrderItemId emptyPurchaseOrderItemId = new PurchaseOrderItemId();

        //when
        assertAll("PurchaseOrderItemID fields validation",
                () -> assertEquals(purchaseOrderItemId1.getPurchaseOrderId(), purchaseOrderItemId2.getPurchaseOrderId(), "Purchase Order Item ID does not equal"),
                () -> assertEquals(purchaseOrderItemId1.getOrdinalNumber(), purchaseOrderItemId2.getOrdinalNumber(), "Purchase Order Item ordinal number does not equal"));

        assertTrue(isequal, "Purchase Order Item ID object are not equal");
        assertTrue(hasCode, "Purchase Order Item ID hashCode are not equal");
        assertNotNull(emptyPurchaseOrderItemId, "Empty Purchase Order Item ID object should not be null");

    }
}