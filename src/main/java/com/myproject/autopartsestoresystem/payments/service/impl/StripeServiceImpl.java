package com.myproject.autopartsestoresystem.payments.service.impl;

import com.myproject.autopartsestoresystem.payments.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.param.ChargeCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Miroslav Kološnjaji
 */
@Service
public class StripeServiceImpl implements StripeService {

    @Value("$(stripe.api.key)")
    private String stripeApiKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public Charge chargeCreditCard(String token, BigDecimal amount) throws StripeException {
        ChargeCreateParams params = ChargeCreateParams.builder()
                .setAmount(Long.valueOf(String.valueOf(amount)))
                .setCurrency("USD")
                .setDescription("Sample Charge")
                .setSource(token)
                .build();

        return Charge.create(params);
    }
}
