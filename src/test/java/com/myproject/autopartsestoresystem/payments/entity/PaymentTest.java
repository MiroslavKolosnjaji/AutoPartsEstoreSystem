package com.myproject.autopartsestoresystem.payments.entity;

import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PaymentTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {

       Payment payment1 = Payment.builder()
                .id(1L)
                .paymentMethod(PaymentMethod.builder().build())
                .purchaseOrder(PurchaseOrder.builder().build())
                .status(PaymentStatus.PROCESSING)
                .amount(new BigDecimal("123.00"))
                .build();

        Payment payment2 = Payment.builder()
                .id(1L)
                .paymentMethod(PaymentMethod.builder().build())
                .purchaseOrder(PurchaseOrder.builder().build())
                .status(PaymentStatus.PROCESSING)
                .amount(new BigDecimal("123.00"))
                .build();

        boolean equals = payment1.equals(payment2);
        boolean hashCode = payment1.hashCode() == payment2.hashCode();

        assertAll("Payment fields validation",
                () -> assertEquals(payment1.getId(), payment2.getId(), "Payment ID mismatch"),
                () -> assertEquals(payment1.getPaymentMethod(), payment2.getPaymentMethod(), "Payment method mismatch"),
                () -> assertEquals(payment1.getPurchaseOrder(), payment2.getPurchaseOrder(), "Payment purchaseOrder mismatch"),
                () -> assertEquals(payment1.getStatus(), payment2.getStatus(), "Payment status mismatch"),
                () -> assertEquals(payment1.getAmount(), payment2.getAmount(), "Payment amount mismatch"));

        assertTrue(equals, "Payment should be equal");
        assertTrue(hashCode, "Payment hashCode mismatch");
    }
}