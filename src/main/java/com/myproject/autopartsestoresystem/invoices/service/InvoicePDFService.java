package com.myproject.autopartsestoresystem.invoices.service;

import com.myproject.autopartsestoresystem.invoices.exception.InvoicePDFGenerationFailedException;

/**
 * @author Miroslav Kološnjaji
 */
public interface InvoicePDFService {

    void generateAndSendInvoicePDF(Long invoiceId, String recipientEmail) throws InvoicePDFGenerationFailedException;

}
