package com.myproject.autopartsestoresystem.payments.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PaymentMethodTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {

        //given
        PaymentMethod paymentMethod1 = PaymentMethod.builder().id(1L).paymentType(PaymentType.DEBIT_CARD).build();
        PaymentMethod paymentMethod2 = PaymentMethod.builder().id(1L).paymentType(PaymentType.DEBIT_CARD).build();

        boolean equals = paymentMethod1.equals(paymentMethod2);
        boolean hashCodeEquals = paymentMethod1.hashCode() == paymentMethod2.hashCode();

        //when && then

        assertAll("PaymentMethod fields validation",
                () -> assertEquals(paymentMethod1.getId(), paymentMethod2.getId(), "Payment Method ID mismatch"),
                () -> assertEquals(paymentMethod1.getPaymentType(), paymentMethod2.getPaymentType(), "Payment Method Type mismatch"));

        assertTrue(equals, "Payment methods should be equal");
        assertTrue(hashCodeEquals, "Payment method hash codes should be equal");
    }
}