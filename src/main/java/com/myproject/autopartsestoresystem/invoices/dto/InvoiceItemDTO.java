package com.myproject.autopartsestoresystem.invoices.dto;

import com.myproject.autopartsestoresystem.invoices.entity.Invoice;
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

    @NotNull(message = "Quantity required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    @NotNull(message = "Unit price required")
    @PositiveOrZero(message = "Unit price must be zero or above")
    private BigDecimal unitPrice;

    @NotNull(message = "Total price required")
    @PositiveOrZero(message = "Total price must be zero or above")
    private BigDecimal totalPrice;

    @NotNull(message = "Discount percentage required")
    @DecimalMin(value = "0.0", message = "Discount must be at least 0")
    @DecimalMax(value = "100.0", message = "Discount must be at most 100")
    private BigDecimal discountPercentage;

    @NotNull(message = "Tax percentage required")
    @DecimalMin(value = "0.0", message = "Tax must be at least 0")
    @DecimalMax(value = "100.0", message = "Tax must be at most 100")
    private BigDecimal taxPercentage;

    @NotNull(message = "Invoice is required")
    private Invoice invoice;
}
