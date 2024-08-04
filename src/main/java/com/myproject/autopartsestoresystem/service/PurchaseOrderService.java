package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.exception.service.PaymentProcessingException;
import com.myproject.autopartsestoresystem.exception.service.PurchaseOrderItemNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.PurchaseOrderNotFoundException;
import com.myproject.autopartsestoresystem.model.PurchaseOrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Miroslav Kološnjaji
 */
public interface PurchaseOrderService extends CrudService<PurchaseOrderDTO, Long> {

    PurchaseOrderDTO savePurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws PurchaseOrderItemNotFoundException, PaymentProcessingException;
    PurchaseOrderDTO findByPurchaseOrderNumber(UUID purchaseOrderNumber) throws PurchaseOrderNotFoundException;
    Optional<List<PurchaseOrderDTO>> findByCustomerId(Long customerId);
    void updateOrderStatus(UUID purchaseOrderNumber, PurchaseOrderStatus purchaseOrderStatus) throws PurchaseOrderNotFoundException;


}
