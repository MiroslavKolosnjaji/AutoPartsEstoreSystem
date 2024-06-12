package com.myproject.autopartsestoresystem.exception.service;

/**
 * @author Miroslav Kološnjaji
 */
public class CityAlreadyExists extends RuntimeException {

    public CityAlreadyExists() {
    }

    public CityAlreadyExists(String message) {
        super(message);
    }

    public CityAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }

    public CityAlreadyExists(Throwable cause) {
        super(cause);
    }

    public CityAlreadyExists(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
