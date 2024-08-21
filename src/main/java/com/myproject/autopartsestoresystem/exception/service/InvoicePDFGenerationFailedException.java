package com.myproject.autopartsestoresystem.exception.service;

/**
 * @author Miroslav Kološnjaji
 */
public class InvoicePDFGenerationFailedException extends Exception{

    public InvoicePDFGenerationFailedException() {
    }

    public InvoicePDFGenerationFailedException(String message) {
        super(message);
    }

    public InvoicePDFGenerationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvoicePDFGenerationFailedException(Throwable cause) {
        super(cause);
    }

    public InvoicePDFGenerationFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
