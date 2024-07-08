package com.myproject.autopartsestoresystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {

    @NotNull(message = "Cart id in item cannot be null")
    private Long cartId;

    @NotNull(message = "Ordinal num cannot be null")
    @Min(value = 1, message = "Ordinal num starts from 1")
    private Integer ordinalNumber;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must have at least 1")
    private Integer quantity;

    @NotNull(message = "Unit price cannot be null")
    @Positive
    private BigDecimal unitPrice;

    @NotNull(message = "Total price cannot be null")
    @Positive
    private BigDecimal totalPrice;

}
