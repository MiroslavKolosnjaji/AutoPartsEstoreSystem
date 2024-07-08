package com.myproject.autopartsestoresystem.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
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
@Embeddable
public class Item {

    @EmbeddedId
    private ItemId id;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

}
