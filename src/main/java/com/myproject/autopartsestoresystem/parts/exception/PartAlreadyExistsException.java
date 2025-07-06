package com.myproject.autopartsestoresystem.parts.exception;

import com.myproject.autopartsestoresystem.common.exception.controller.EntityAlreadyExistsException;

/**
 * @author Miroslav Kološnjaji
 */
public class PartAlreadyExistsException extends EntityAlreadyExistsException {

    public PartAlreadyExistsException() {
    }

    public PartAlreadyExistsException(String message) {
        super(message);
    }

    public PartAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public PartAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public PartAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
