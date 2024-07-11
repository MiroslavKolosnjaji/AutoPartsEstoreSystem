package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.PurchaseOrderItemDTO;
import com.myproject.autopartsestoresystem.model.PurchaseOrderItemId;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
public interface PurchaseOrderItemService extends CrudService<PurchaseOrderItemDTO, PurchaseOrderItemId>{

    List<PurchaseOrderItemDTO> findByPurchaseOrderId(Long purchaseOrderId);
    List<PurchaseOrderItemDTO> saveAll(Long purchaseOrderId, List<PurchaseOrderItemDTO> purchaseOrderItemDTOList);
    List<PurchaseOrderItemDTO> updateItemList(Long purchaseOrderId, List<PurchaseOrderItemDTO> purchaseOrderItemDTOList);
}
