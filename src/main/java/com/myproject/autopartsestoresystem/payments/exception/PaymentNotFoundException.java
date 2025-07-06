package com.myproject.autopartsestoresystem.payments.exception;

import com.myproject.autopartsestoresystem.common.exception.controller.EntityNotFoundException;

/**
 * @author Miroslav Kološnjaji
 */
public class PaymentNotFoundException extends EntityNotFoundException {

    public PaymentNotFoundException() {
    }

    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymentNotFoundException(Throwable cause) {
        super(cause);
    }

    public PaymentNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
