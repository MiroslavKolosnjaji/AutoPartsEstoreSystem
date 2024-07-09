package com.myproject.autopartsestoresystem.exception.service;

/**
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderItemNotFoundException extends RuntimeException {

    public PurchaseOrderItemNotFoundException() {
    }

    public PurchaseOrderItemNotFoundException(String message) {
        super(message);
    }

    public PurchaseOrderItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PurchaseOrderItemNotFoundException(Throwable cause) {
        super(cause);
    }

    public PurchaseOrderItemNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
