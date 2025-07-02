package com.myproject.autopartsestoresystem.parts.exception;

import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;

/**
 * @author Miroslav Kološnjaji
 */
public class PartNotFoundException extends EntityNotFoundException {

    public PartNotFoundException() {
    }

    public PartNotFoundException(String message) {
        super(message);
    }

    public PartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PartNotFoundException(Throwable cause) {
        super(cause);
    }

    public PartNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
