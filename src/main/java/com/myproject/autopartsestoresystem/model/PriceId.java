package com.myproject.autopartsestoresystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PriceId {

    @NotNull(groups = Update.class, message = "PRICE ID - part id cannot be null")
    @Column(name = "part_id")
    private Long partId;

    @Column(name = "price_id")
    private Long priceId;
}
