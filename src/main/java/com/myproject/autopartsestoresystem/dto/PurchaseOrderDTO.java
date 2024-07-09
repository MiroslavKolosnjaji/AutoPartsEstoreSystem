package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.PurchaseOrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @NotNull(message = "Purchase order number cannot be null")
    private UUID purchaseOrderNumber;

    @NotNull(message = "Status cannot be null")
    private PurchaseOrderStatus status;

    @NotNull(message = "Total amount cannot be null")
    @Positive(message = "Total amount cannot be negative")
    private BigDecimal totalAmount;

    @NotNull(message = "Item list cannot be null")
    private List<PurchaseOrderItemDTO> items;

    private CustomerDTO customer;
}
