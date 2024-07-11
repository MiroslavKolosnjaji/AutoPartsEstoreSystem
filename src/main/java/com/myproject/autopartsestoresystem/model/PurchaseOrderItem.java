package com.myproject.autopartsestoresystem.model;

import jakarta.persistence.*;
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
@Entity
@Table(name = "purchaseOrderItem")
public class PurchaseOrderItem {

    @EmbeddedId
    private PurchaseOrderItemId id;

    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("purchaseOrderId")
    @JoinColumn(name = "purchaseOrderId", insertable = false, updatable = false)
    private PurchaseOrder purchaseOrder;

}
