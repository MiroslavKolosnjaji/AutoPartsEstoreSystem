package com.myproject.autopartsestoresystem.exception.service;

/**
 * @author Miroslav Kološnjaji
 */
public class PaymentProcessingException extends Exception {

    public PaymentProcessingException() {
    }

    public PaymentProcessingException(String message) {
        super(message);
    }

    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentProcessingException(Throwable cause) {
        super(cause);
    }

    public PaymentProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

