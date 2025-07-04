package com.myproject.autopartsestoresystem.invoices.mapper;

import com.myproject.autopartsestoresystem.invoices.dto.InvoiceItemDTO;
import com.myproject.autopartsestoresystem.invoices.entity.InvoiceItem;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface InvoiceItemMapper {

    InvoiceItemDTO invoiceItemToInvoiceItemDTO(InvoiceItem invoiceItem);
    InvoiceItem invoiceItemDTOToInvoiceItem(InvoiceItemDTO invoiceItemDTO);
}
