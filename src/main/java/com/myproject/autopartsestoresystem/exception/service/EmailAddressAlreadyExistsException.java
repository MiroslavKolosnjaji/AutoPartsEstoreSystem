package com.myproject.autopartsestoresystem.exception.service;

import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;

/**
 * @author Miroslav Kološnjaji
 */
public class EmailAddressAlreadyExistsException extends EntityAlreadyExistsException {

    public EmailAddressAlreadyExistsException() {
    }

    public EmailAddressAlreadyExistsException(String message) {
        super(message);
    }

    public EmailAddressAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailAddressAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public EmailAddressAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
