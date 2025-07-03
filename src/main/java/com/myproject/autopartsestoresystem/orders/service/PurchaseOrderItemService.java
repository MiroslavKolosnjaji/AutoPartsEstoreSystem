package com.myproject.autopartsestoresystem.orders.service;

import com.myproject.autopartsestoresystem.orders.dto.PurchaseOrderItemDTO;
import com.myproject.autopartsestoresystem.orders.exception.PurchaseOrderItemNotFoundException;
import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrderItemId;
import com.myproject.autopartsestoresystem.service.CrudService;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
public interface PurchaseOrderItemService extends CrudService<PurchaseOrderItemDTO, PurchaseOrderItemId> {

    List<PurchaseOrderItemDTO> findByPurchaseOrderId(Long purchaseOrderId);
    List<PurchaseOrderItemDTO> saveAll(Long purchaseOrderId, List<PurchaseOrderItemDTO> purchaseOrderItemDTOList);
    List<PurchaseOrderItemDTO> updateItemList(Long purchaseOrderId, List<PurchaseOrderItemDTO> purchaseOrderItemDTOList) throws PurchaseOrderItemNotFoundException;
}
