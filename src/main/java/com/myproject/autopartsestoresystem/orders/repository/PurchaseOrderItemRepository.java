package com.myproject.autopartsestoresystem.orders.repository;

import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrderItem;
import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

/**
 * @author Miroslav Kološnjaji
 */
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, PurchaseOrderItemId> {
   Optional<Set<PurchaseOrderItem>> findByPurchaseOrderId(Long purchaseOrderId);
}
