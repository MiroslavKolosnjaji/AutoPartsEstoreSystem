package com.myproject.autopartsestoresystem.orders.mapper;

import com.myproject.autopartsestoresystem.orders.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {

    @Mapping(target = "items", source = "purchaseOrder.purchaseOrderItems")
    PurchaseOrderDTO purchaseOrderToPurchaseOrderDTO(PurchaseOrder purchaseOrder);

    @Mapping(target = "purchaseOrderItems", source = "items")
    PurchaseOrder purchaseOrderDTOtoPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO);
}
