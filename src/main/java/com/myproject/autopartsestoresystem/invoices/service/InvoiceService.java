package com.myproject.autopartsestoresystem.invoices.service;

import com.myproject.autopartsestoresystem.invoices.dto.InvoiceDTO;
import com.myproject.autopartsestoresystem.invoices.exception.InvoiceCreationException;
import com.myproject.autopartsestoresystem.invoices.exception.InvoiceNotFoundException;

/**
 * @author Miroslav Kološnjaji
 */
public interface InvoiceService {

    void createInvoice(Long purchaseOrderId, Long storeId) throws InvoiceCreationException;
    InvoiceDTO getInvoice(Long id) throws InvoiceNotFoundException;
}
