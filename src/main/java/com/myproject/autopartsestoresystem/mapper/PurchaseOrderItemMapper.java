package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.PurchaseOrderItemDTO;
import com.myproject.autopartsestoresystem.model.PurchaseOrderItem;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface PurchaseOrderItemMapper {

    @Mapping(target = "purchaseOrderId", source = "id.purchaseOrderId")
    @Mapping(target = "ordinalNumber", source = "id.ordinalNumber")
    PurchaseOrderItemDTO purchaseOrderItemToPurchaseOrderItemDTO(PurchaseOrderItem purchaseOrderItem);

    @Mapping(target = "id.purchaseOrderId", source = "purchaseOrderId")
    @Mapping(target = "id.ordinalNumber", source = "ordinalNumber")
    PurchaseOrderItem purchaseOrderItemDTOToPurchaseOrderItem(PurchaseOrderItemDTO purchaseOrderItemDTO);

    @IterableMapping(elementTargetType = PurchaseOrderItemDTO.class)
    List<PurchaseOrderItemDTO> purchaseOrderItemListToPurchaseOrderDTOList(List<PurchaseOrderItem> purchaseOrderItemList);

    @IterableMapping(elementTargetType = PurchaseOrderItem.class)
    List<PurchaseOrderItem> purchaseOrderItemDTOListToPurchaseOrderItemList(List<PurchaseOrderItemDTO> purchaseOrderDTOList);

    @IterableMapping(elementTargetType = PurchaseOrderItemDTO.class)
    List<PurchaseOrderItemDTO> purchaseOrderItemSetToPurchaseOrderItemDTOList(Set<PurchaseOrderItem> purchaseOrderItemList);

    @IterableMapping(elementTargetType = PurchaseOrderItem.class)
    List<PurchaseOrderItem> purchaseOrderItemSetToPurchaseOrderItemList(Set<PurchaseOrderItem> purchaseOrderItemList);
}
