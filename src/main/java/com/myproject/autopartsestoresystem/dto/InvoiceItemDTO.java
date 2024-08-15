package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.Invoice;
import jakarta.validation.constraints.*;
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
public class InvoiceItemDTO {

    @NotNull(groups = Update.class, message = "Id is required for invoice item")
    private Long id;

    @NotBlank(message = "Product name required")
    private String productName;

    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    @PositiveOrZero(message = "Unit price must be zero or above")
    private BigDecimal unitPrice;

    @PositiveOrZero(message = "Total price must be zero or above")
    private BigDecimal totalPrice;

    @DecimalMin(value = "0.0", message = "Discount must be at least 0")
    @DecimalMax(value = "100.0", message = "Discount must be at most 100")
    private BigDecimal discountPercentage;

    @DecimalMin(value = "0.0", message = "Tax must be at least 0")
    @DecimalMax(value = "100.0", message = "Tax must be at most 100")
    private BigDecimal taxPercentage;

    @NotNull(message = "Invoice is required")
    private Invoice invoice;
}
