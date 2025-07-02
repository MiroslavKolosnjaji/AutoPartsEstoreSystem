package com.myproject.autopartsestoresystem.parts.exception;

import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;

/**
 * @author Miroslav Kološnjaji
 */
public class PartGroupAlreadyExistsException extends EntityAlreadyExistsException {

    public PartGroupAlreadyExistsException() {
    }

    public PartGroupAlreadyExistsException(String message) {
        super(message);
    }

    public PartGroupAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public PartGroupAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public PartGroupAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
