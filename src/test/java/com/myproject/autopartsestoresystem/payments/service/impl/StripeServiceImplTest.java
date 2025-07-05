package com.myproject.autopartsestoresystem.payments.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class StripeServiceImplTest {

    @InjectMocks
    private StripeServiceImpl stripeService;


    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(stripeService, "stripeApiKey", System.getenv("STRIPE_API_KEY"));
        stripeService.init();
    }

    @Test
    void testChargeCreditCardSuccess() throws StripeException {

        try(MockedStatic<Charge> mockedCharge = mockStatic(Charge.class)){
            Charge mockCharge = new Charge();
            mockCharge.setStatus("succeeded");
            mockCharge.setAmount(2000L);
            mockCharge.setCurrency("USD");

            mockedCharge.when(() -> Charge.create(any(ChargeCreateParams.class))).thenReturn(mockCharge);

            BigDecimal amount = BigDecimal.valueOf(2000);
            Charge charge = stripeService.chargeCreditCard("tok_visa", amount);

            assertEquals("succeeded", charge.getStatus());
            assertEquals(2000, charge.getAmount());
            assertEquals("USD", charge.getCurrency());

        }
    }
}