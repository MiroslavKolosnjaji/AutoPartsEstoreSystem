package com.myproject.autopartsestoresystem.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PurchaseOrderItemId {

    private Long purchaseOrderId;
    private Integer ordinalNumber;
}
