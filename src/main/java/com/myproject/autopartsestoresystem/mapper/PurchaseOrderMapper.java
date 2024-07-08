package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.model.PurchaseOrder;
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
