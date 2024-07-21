package com.myproject.autopartsestoresystem.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class PaymentStatusTest {

    @ParameterizedTest
    @EnumSource(PaymentStatus.class)
    void testAllFields_isEqual_ThenCorrect(PaymentStatus paymentStatus) {
        assertEquals(paymentStatus, PaymentStatus.valueOf(paymentStatus.name()));
    }
}