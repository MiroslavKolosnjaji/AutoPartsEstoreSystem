package com.myproject.autopartsestoresystem.orders.service;

import com.myproject.autopartsestoresystem.orders.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.payments.exception.PaymentProcessingException;
import com.myproject.autopartsestoresystem.orders.exception.PurchaseOrderItemNotFoundException;
import com.myproject.autopartsestoresystem.orders.exception.PurchaseOrderNotFoundException;
import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrderStatus;
import com.myproject.autopartsestoresystem.service.CrudService;

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
