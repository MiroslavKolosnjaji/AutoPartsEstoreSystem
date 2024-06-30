package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.Currency;
import com.myproject.autopartsestoresystem.model.PriceId;
import jakarta.persistence.EmbeddedId;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.sql.Timestamp;
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

    @NotNull(message = "Date modified cannot be null")
    private LocalDateTime dateModified;
}
