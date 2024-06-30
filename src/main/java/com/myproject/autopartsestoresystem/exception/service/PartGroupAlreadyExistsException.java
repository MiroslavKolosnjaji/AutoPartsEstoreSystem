package com.myproject.autopartsestoresystem.exception.service;

/**
 * @author Miroslav Kološnjaji
 */
public class PartGroupAlreadyExistsException extends RuntimeException {

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
