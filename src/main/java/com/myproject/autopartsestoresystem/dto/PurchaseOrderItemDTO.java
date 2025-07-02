package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.PurchaseOrder;
import com.myproject.autopartsestoresystem.parts.dto.PartDTO;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import java.math.BigDecimal;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrderItemDTO {

    @NotNull(groups = Update.class, message = "Purchase order id in purchase item cannot be null")
    private Long purchaseOrderId;

    @NotNull(groups = Update.class, message = "Ordinal num cannot be null")
    @Min(value = 1, message = "Ordinal num starts from 1")
    private Integer ordinalNumber;

    @NotNull(message = "Part cannot be null")
    private PartDTO part;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity cannot be less than 1")
    private Integer quantity;

    @NotNull(groups = Update.class, message = "Unit price cannot be null")
    @Positive(message = "Unit price cannot be negative")
    private BigDecimal unitPrice;

    @NotNull(groups = Update.class, message = "Total price cannot be null")
    @Positive(message = "Total price cannot be negative")
    private BigDecimal totalPrice;

    private PurchaseOrder purchaseOrder;

}
