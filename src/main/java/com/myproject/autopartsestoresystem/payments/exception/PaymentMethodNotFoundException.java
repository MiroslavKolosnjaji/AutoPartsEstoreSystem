package com.myproject.autopartsestoresystem.payments.exception;

import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;

/**
 * @author Miroslav Kološnjaji
 */
public class PaymentMethodNotFoundException extends EntityNotFoundException {

    public PaymentMethodNotFoundException() {

    }

    public PaymentMethodNotFoundException(String message) {
        super(message);
    }

    public PaymentMethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentMethodNotFoundException(Throwable cause) {
        super(cause);
    }

    public PaymentMethodNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
