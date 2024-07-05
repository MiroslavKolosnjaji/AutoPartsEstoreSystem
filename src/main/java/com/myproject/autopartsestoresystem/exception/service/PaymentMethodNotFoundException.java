package com.myproject.autopartsestoresystem.exception.service;

/**
 * @author Miroslav Kološnjaji
 */
public class PaymentMethodNotFoundException extends RuntimeException {

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
