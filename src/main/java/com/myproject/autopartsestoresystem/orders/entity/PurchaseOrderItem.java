package com.myproject.autopartsestoresystem.orders.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myproject.autopartsestoresystem.parts.entity.Part;
import jakarta.persistence.*;
import lombok.*;

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
    @JsonIgnore
    private Part part;

    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("purchaseOrderId")
    @JoinColumn(name = "purchase_order_id", insertable = false, updatable = false)
    @JsonBackReference
    private PurchaseOrder purchaseOrder;

}
