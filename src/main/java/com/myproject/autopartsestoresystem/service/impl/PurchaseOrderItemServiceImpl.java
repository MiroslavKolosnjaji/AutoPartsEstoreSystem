package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.PartDTO;
import com.myproject.autopartsestoresystem.dto.PurchaseOrderItemDTO;
import com.myproject.autopartsestoresystem.exception.service.PurchaseOrderItemNotFoundException;
import com.myproject.autopartsestoresystem.mapper.PurchaseOrderItemMapper;
import com.myproject.autopartsestoresystem.model.Part;
import com.myproject.autopartsestoresystem.model.Price;
import com.myproject.autopartsestoresystem.model.PurchaseOrderItem;
import com.myproject.autopartsestoresystem.model.PurchaseOrderItemId;
import com.myproject.autopartsestoresystem.repository.PurchaseOrderItemRepository;
import com.myproject.autopartsestoresystem.service.PartService;
import com.myproject.autopartsestoresystem.service.PriceService;
import com.myproject.autopartsestoresystem.service.PurchaseOrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class PurchaseOrderItemServiceImpl implements PurchaseOrderItemService {

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final PurchaseOrderItemMapper purchaseOrderItemMapper;

    private static final int DEFAULT_VALUE = 1;


    @Override
    public List<PurchaseOrderItemDTO> findByPurchaseOrderId(Long purchaseOrderId) {
        return List.of();
    }

    @Override
    public List<PurchaseOrderItemDTO> saveAll(Long purchaseOrderId, List<PurchaseOrderItemDTO> purchaseOrderItemDTOList) {


        updateTotalPriceAndOrdinalNumber(purchaseOrderId, purchaseOrderItemDTOList);

        List<PurchaseOrderItem> preparedList = purchaseOrderItemMapper.purchaseOrderItemDTOListToPurchaseOrderItemList(purchaseOrderItemDTOList);

        List<PurchaseOrderItem> savedList = purchaseOrderItemRepository.saveAll(preparedList);

        return purchaseOrderItemMapper.purchaseOrderItemListToPurchaseOrderDTOList(savedList);
    }



    @Override
    public List<PurchaseOrderItemDTO> updateItemList(Long purchaseOrderId, List<PurchaseOrderItemDTO> purchaseOrderItemDTOList) throws PurchaseOrderItemNotFoundException {

        Set<PurchaseOrderItem> purchaseOrderItems = purchaseOrderItemRepository.findByPurchaseOrderId(purchaseOrderId)
                .orElseThrow(PurchaseOrderItemNotFoundException::new);

        updateTotalPriceAndOrdinalNumber(purchaseOrderId, purchaseOrderItemDTOList);

        purchaseOrderItems.removeIf( item -> !purchaseOrderItemDTOList.contains(item));
        purchaseOrderItemDTOList.forEach(item -> purchaseOrderItems.add(purchaseOrderItemMapper.purchaseOrderItemDTOToPurchaseOrderItem(item)));

        List<PurchaseOrderItem> updated =  purchaseOrderItemRepository.saveAll(purchaseOrderItems);

        return purchaseOrderItemMapper.purchaseOrderItemListToPurchaseOrderDTOList(updated);
    }

    @Override
    public PurchaseOrderItemDTO save(PurchaseOrderItemDTO purchaseOrderItemDTO) {

        updateItemValues(purchaseOrderItemDTO, DEFAULT_VALUE);

        PurchaseOrderItem saved = purchaseOrderItemRepository.save(purchaseOrderItemMapper.purchaseOrderItemDTOToPurchaseOrderItem(purchaseOrderItemDTO));

        return purchaseOrderItemMapper.purchaseOrderItemToPurchaseOrderItemDTO(saved);
    }

    @Override
    public PurchaseOrderItemDTO update(PurchaseOrderItemId purchaseOrderItemId, PurchaseOrderItemDTO purchaseOrderItemDTO) {
        return null;
    }

    @Override
    public List<PurchaseOrderItemDTO> getAll() {
        return purchaseOrderItemRepository.findAll().stream()
                .map(purchaseOrderItemMapper::purchaseOrderItemToPurchaseOrderItemDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseOrderItemDTO getById(PurchaseOrderItemId purchaseOrderItemId) throws PurchaseOrderItemNotFoundException {

        return purchaseOrderItemRepository.findById(purchaseOrderItemId)
                .map(purchaseOrderItemMapper::purchaseOrderItemToPurchaseOrderItemDTO)
                .orElseThrow(() -> new PurchaseOrderItemNotFoundException("Purchase order item not found"));
    }

    @Override
    public void delete(PurchaseOrderItemId purchaseOrderItemId) {
            purchaseOrderItemRepository.deleteById(purchaseOrderItemId);
    }

    private void updateTotalPriceAndOrdinalNumber(Long purchaseOrderId, List<PurchaseOrderItemDTO> purchaseOrderItemDTOList) {
        int itemCount = DEFAULT_VALUE;
        for (PurchaseOrderItemDTO purchaseOrderItemDTO : purchaseOrderItemDTOList) {
            purchaseOrderItemDTO.setPurchaseOrderId(purchaseOrderId);
            updateItemValues(purchaseOrderItemDTO, itemCount++);
        }
    }

    private static void updateItemValues(PurchaseOrderItemDTO purchaseOrderItemDTO, int itemCount) {

        int indexOfLastPrice = purchaseOrderItemDTO.getPart().getPrices().size() - 1;
        BigDecimal lastPrice = purchaseOrderItemDTO.getPart().getPrices().get(indexOfLastPrice).getPrice();

        purchaseOrderItemDTO.setUnitPrice(lastPrice);
        purchaseOrderItemDTO.setOrdinalNumber(itemCount);
        purchaseOrderItemDTO.setTotalPrice(purchaseOrderItemDTO.getUnitPrice().multiply(BigDecimal.valueOf(purchaseOrderItemDTO.getQuantity())));
    }
}
