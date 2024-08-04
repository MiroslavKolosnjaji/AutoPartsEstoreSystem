package com.myproject.autopartsestoresystem.exception.service;

import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;

/**
 * @author Miroslav Kološnjaji
 */
public class PurchaseOrderItemNotFoundException extends EntityNotFoundException {

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
