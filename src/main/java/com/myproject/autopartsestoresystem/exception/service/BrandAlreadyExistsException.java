package com.myproject.autopartsestoresystem.exception.service;

/**
 * @author Miroslav Kološnjaji
 */
public class BrandAlreadyExistsException extends RuntimeException {

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
