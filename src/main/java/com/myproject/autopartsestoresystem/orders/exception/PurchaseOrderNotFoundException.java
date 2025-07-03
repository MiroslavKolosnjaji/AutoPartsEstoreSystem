package com.myproject.autopartsestoresystem.orders.exception;

import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;

/**
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderNotFoundException extends EntityNotFoundException {

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
