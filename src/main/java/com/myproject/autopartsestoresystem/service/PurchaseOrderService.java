package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.model.PurchaseOrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Miroslav Kološnjaji
 */
public interface PurchaseOrderService extends CrudService<PurchaseOrderDTO, Long> {

    PurchaseOrderDTO findByCartNumber(UUID cartNumber);
    Optional<List<PurchaseOrderDTO>> findByCustomerId(Long customerId);
    void updateCartStatus(UUID cartNumber, PurchaseOrderStatus purchaseOrderStatus);


}
