package com.myproject.autopartsestoresystem.invoices.mapper;

import com.myproject.autopartsestoresystem.invoices.dto.InvoiceDTO;
import com.myproject.autopartsestoresystem.invoices.entity.Invoice;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface InvoiceMapper {

    InvoiceDTO invoiceToInvoiceDTO(Invoice invoice);
    Invoice invoiceDTOToInvoice(InvoiceDTO invoice);
}
