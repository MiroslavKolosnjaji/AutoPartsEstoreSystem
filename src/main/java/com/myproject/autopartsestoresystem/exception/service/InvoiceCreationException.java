package com.myproject.autopartsestoresystem.exception.service;

/**
 * @author Miroslav Kološnjaji
 */
public class InvoiceCreationException extends Exception{

    public InvoiceCreationException() {
    }

    public InvoiceCreationException(String message) {
        super(message);
    }

    public InvoiceCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvoiceCreationException(Throwable cause) {
        super(cause);
    }

    public InvoiceCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
