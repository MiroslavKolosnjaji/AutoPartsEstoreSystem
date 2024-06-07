package com.myproject.autopartsestoresystem.exception;

/**
 * @author Miroslav Kološnjaji
 */
public class CustomerDoesntExistsException extends RuntimeException {

    public CustomerDoesntExistsException() {
    }

    public CustomerDoesntExistsException(String message) {
        super(message);
    }

    public CustomerDoesntExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerDoesntExistsException(Throwable cause) {
        super(cause);
    }

    public CustomerDoesntExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
