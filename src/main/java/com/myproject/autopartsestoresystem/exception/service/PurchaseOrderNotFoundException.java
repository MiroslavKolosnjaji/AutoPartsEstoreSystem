package com.myproject.autopartsestoresystem.exception.service;

/**
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderNotFoundException extends RuntimeException {

    public PurchaseOrderNotFoundException() {
    }

    public PurchaseOrderNotFoundException(String message) {
        super(message);
    }

    public PurchaseOrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PurchaseOrderNotFoundException(Throwable cause) {
        super(cause);
    }

    public PurchaseOrderNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
