package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.InvoiceDTO;
import com.myproject.autopartsestoresystem.exception.service.InvoicePDFGenerationFailedException;

import java.io.IOException;

/**
 * @author Miroslav Kološnjaji
 */
public interface InvoicePDFService {

    byte[] generateInvoicePDF(InvoiceDTO invoiceDTO) throws InvoicePDFGenerationFailedException;
    void generatePDFToFile(InvoiceDTO invoiceDTO) throws InvoicePDFGenerationFailedException, IOException;
}
