package com.myproject.autopartsestoresystem.brands.exception;

import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;

/**
 * @author Miroslav Kološnjaji
 */
public class BrandAlreadyExistsException extends EntityAlreadyExistsException {

    public BrandAlreadyExistsException() {
    }

    public BrandAlreadyExistsException(String message) {
        super(message);
    }

    public BrandAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrandAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public BrandAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
