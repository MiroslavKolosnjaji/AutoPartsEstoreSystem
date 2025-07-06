package com.myproject.autopartsestoresystem.customers.exception;

import com.myproject.autopartsestoresystem.common.exception.controller.EntityNotFoundException;

/**
 * @author Miroslav Kološnjaji
 */
public class CustomerNotFoundException extends EntityNotFoundException {

    public CustomerNotFoundException() {
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }

    public CustomerNotFoundException(Long id){
        super("Customer with id " + id + " does not exist");
    }

    public CustomerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerNotFoundException(Throwable cause) {
        super(cause);
    }

    public CustomerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
