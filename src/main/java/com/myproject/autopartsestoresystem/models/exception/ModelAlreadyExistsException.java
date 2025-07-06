package com.myproject.autopartsestoresystem.models.exception;

import com.myproject.autopartsestoresystem.common.exception.controller.EntityAlreadyExistsException;

/**
 * @author Miroslav Kološnjaji
 */
public class ModelAlreadyExistsException extends EntityAlreadyExistsException {

    public ModelAlreadyExistsException() {
    }

    public ModelAlreadyExistsException(String message) {
        super(message);
    }

    public ModelAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public ModelAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
