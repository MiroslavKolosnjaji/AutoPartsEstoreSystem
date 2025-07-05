package com.myproject.autopartsestoresystem.invoices.entity;

import com.myproject.autopartsestoresystem.stores.entity.Store;
import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne
    private PurchaseOrder purchaseOrder;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItem> invoiceItems;

    private BigDecimal totalAmount;

}
