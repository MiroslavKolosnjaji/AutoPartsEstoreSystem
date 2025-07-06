package com.myproject.autopartsestoresystem.brands.exception;

import com.myproject.autopartsestoresystem.common.exception.controller.EntityNotFoundException;

/**
 * @author Miroslav Kološnjaji
 */
public class BrandNotFoundException extends EntityNotFoundException {

    public BrandNotFoundException() {
    }

    public BrandNotFoundException(String message) {
        super(message);
    }

    public BrandNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BrandNotFoundException(Throwable cause) {
        super(cause);
    }

    public BrandNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
