package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.InvoiceItemDTO;
import com.myproject.autopartsestoresystem.model.InvoiceItem;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface InvoiceItemMapper {

    InvoiceItemDTO invoiceItemToInvoiceItemDTO(InvoiceItem invoiceItem);
    InvoiceItem invoiceItemDTOToInvoiceItem(InvoiceItemDTO invoiceItemDTO);
}
