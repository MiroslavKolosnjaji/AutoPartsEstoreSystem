package com.myproject.autopartsestoresystem.parts.dto;

import com.myproject.autopartsestoresystem.parts.entity.Currency;
import com.myproject.autopartsestoresystem.parts.entity.PriceId;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceDTO {

    @NotNull(message = "Price ID cannot be null")
    private PriceId id;

    @NotNull(message = "Price cannot be null")
    @Digits(integer = 11, fraction = 2, message = "Price must have up to 11 digits in the integer part and up to 2 digits in the fraction part")
    @PositiveOrZero(message = "Price cannot be negative")
    private BigDecimal price;

    @NotNull(message = "Currency cannot be null")
    private Currency currency;

    private LocalDateTime dateModified;
}
