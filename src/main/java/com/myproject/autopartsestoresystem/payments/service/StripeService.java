package com.myproject.autopartsestoresystem.payments.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import java.math.BigDecimal;

/**
 * @author Miroslav Kološnjaji
 */
public interface StripeService {

    Charge chargeCreditCard(String token, BigDecimal amount) throws StripeException;
}
