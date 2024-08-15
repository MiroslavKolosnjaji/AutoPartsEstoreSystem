package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.InvoiceDTO;
import com.myproject.autopartsestoresystem.model.Invoice;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface InvoiceMapper {

    InvoiceDTO invoiceToInvoiceDTO(Invoice invoice);
    Invoice invoiceDTOToInvoice(InvoiceDTO invoice);
}
