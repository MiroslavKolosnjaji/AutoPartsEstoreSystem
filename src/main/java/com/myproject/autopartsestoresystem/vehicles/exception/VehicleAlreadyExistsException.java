package com.myproject.autopartsestoresystem.vehicles.exception;

import com.myproject.autopartsestoresystem.common.exception.controller.EntityAlreadyExistsException;

/**
 * @author Miroslav Kološnjaji
 */
public class VehicleAlreadyExistsException extends EntityAlreadyExistsException {

    public VehicleAlreadyExistsException() {
    }

    public VehicleAlreadyExistsException(String message) {
        super(message);
    }

    public VehicleAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public VehicleAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public VehicleAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
