package com.myproject.autopartsestoresystem.vehicles.exception;

import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;

/**
 * @author Miroslav Kološnjaji
 */
public class VehicleNotFoundException extends EntityNotFoundException {

    public VehicleNotFoundException() {
    }

    public VehicleNotFoundException(String message) {
        super(message);
    }

    public VehicleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public VehicleNotFoundException(Throwable cause) {
        super(cause);
    }

    public VehicleNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
