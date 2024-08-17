package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.InvoiceItem;
import com.myproject.autopartsestoresystem.model.PurchaseOrder;
import com.myproject.autopartsestoresystem.model.Store;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDTO {

    @NotNull(groups = Update.class, message = "Id required for InvoiceDTO")
    private Long id;

    @NotNull(message = "Store is required")
    private Store store;

    @NotNull(message = "Purchase Order is required")
    private PurchaseOrder purchaseOrder;

    @NotEmpty(message = "Invoice Items are required")
    private List<InvoiceItem> invoiceItems;

    @NotNull(message = "Total Amount required")
    @PositiveOrZero(message = "Total Amount must be zero or above")
    private BigDecimal totalAmount;
}
