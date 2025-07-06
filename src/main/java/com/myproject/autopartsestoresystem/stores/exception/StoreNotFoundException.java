package com.myproject.autopartsestoresystem.stores.exception;

import com.myproject.autopartsestoresystem.common.exception.controller.EntityNotFoundException;

/**
 * @author Miroslav Kološnjaji
 */
public class StoreNotFoundException extends EntityNotFoundException {

    public StoreNotFoundException() {
    }

    public StoreNotFoundException(String message) {
        super(message);
    }

    public StoreNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoreNotFoundException(Throwable cause) {
        super(cause);
    }

    public StoreNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
