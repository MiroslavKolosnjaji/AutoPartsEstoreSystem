package com.myproject.autopartsestoresystem.orders.repository;

import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Miroslav Kološnjaji
 */
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByPurchaseOrderNumber(UUID purchaseOrderNumber);
    Optional<List<PurchaseOrder>> findByCustomerId(Long customerId);
}
