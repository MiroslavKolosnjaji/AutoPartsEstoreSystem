package com.myproject.autopartsestoresystem.customers.exception;

import com.myproject.autopartsestoresystem.common.exception.controller.EntityAlreadyExistsException;

/**
 * @author Miroslav Kološnjaji
 */
public class CustomerAlreadyExistsException extends EntityAlreadyExistsException {

    public CustomerAlreadyExistsException() {
    }

    public CustomerAlreadyExistsException(String message) {
        super(message);
    }

    public CustomerAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public CustomerAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
