package com.myproject.autopartsestoresystem.exception.service;

import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;

/**
 * @author Miroslav Kološnjaji
 */
public class PartGroupNotFoundException extends EntityNotFoundException {
    public PartGroupNotFoundException() {
    }

    public PartGroupNotFoundException(String message) {
        super(message);
    }

    public PartGroupNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PartGroupNotFoundException(Throwable cause) {
        super(cause);
    }

    public PartGroupNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
