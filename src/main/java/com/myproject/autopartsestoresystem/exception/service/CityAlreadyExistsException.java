package com.myproject.autopartsestoresystem.exception.service;

import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;

/**
 * @author Miroslav Kološnjaji
 */
public class CityAlreadyExistsException extends EntityAlreadyExistsException {

    public CityAlreadyExistsException() {
    }

    public CityAlreadyExistsException(String message) {
        super(message);
    }

    public CityAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CityAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public CityAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
