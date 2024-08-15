package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.InvoiceDTO;
import com.myproject.autopartsestoresystem.exception.service.InvoiceCreationException;
import com.myproject.autopartsestoresystem.exception.service.InvoiceNotFoundException;

/**
 * @author Miroslav Kološnjaji
 */
public interface InvoiceService {

    void createInvoice(Long purchaseOrderId, Long storeId) throws InvoiceCreationException;
    InvoiceDTO getInvoice(Long id) throws InvoiceNotFoundException;
}
