package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.InvoiceDTO;
import com.myproject.autopartsestoresystem.exception.service.InvoicePDFGenerationFailedException;

import java.io.IOException;

/**
 * @author Miroslav Kološnjaji
 */
public interface InvoicePDFService {

    void generateAndSendInvoicePDF(Long invoiceId, String recipientEmail) throws InvoicePDFGenerationFailedException;

}
