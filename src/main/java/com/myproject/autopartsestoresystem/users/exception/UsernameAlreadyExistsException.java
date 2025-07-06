package com.myproject.autopartsestoresystem.users.exception;

import com.myproject.autopartsestoresystem.common.exception.controller.EntityAlreadyExistsException;

/**
 * @author Miroslav Kološnjaji
 */
public class UsernameAlreadyExistsException extends EntityAlreadyExistsException {

    public UsernameAlreadyExistsException() {
    }

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

    public UsernameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public UsernameAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
