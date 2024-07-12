package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.dto.PurchaseOrderItemDTO;
import com.myproject.autopartsestoresystem.exception.service.PurchaseOrderNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.PurchaseOrderItemNotFoundException;
import com.myproject.autopartsestoresystem.mapper.PurchaseOrderItemMapper;
import com.myproject.autopartsestoresystem.mapper.PurchaseOrderMapper;
import com.myproject.autopartsestoresystem.model.PurchaseOrder;
import com.myproject.autopartsestoresystem.model.PurchaseOrderItemId;
import com.myproject.autopartsestoresystem.model.PurchaseOrderStatus;
import com.myproject.autopartsestoresystem.model.PurchaseOrderItem;
import com.myproject.autopartsestoresystem.repository.PurchaseOrderRepository;
import com.myproject.autopartsestoresystem.repository.VehicleRepository;
import com.myproject.autopartsestoresystem.service.PurchaseOrderItemService;
import com.myproject.autopartsestoresystem.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemService purchaseOrderItemService;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PurchaseOrderItemMapper purchaseOrderItemMapper;


    @Override
    public PurchaseOrderDTO findByPurchaseOrderNumber(UUID purchaseOrderNumber) {

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByPurchaseOrderNumber(purchaseOrderNumber)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Cart not found"));

        return purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(purchaseOrder);
    }


    @Override
    public void updateOrderStatus(UUID purchaseOrderNumber, PurchaseOrderStatus purchaseOrderStatus) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByPurchaseOrderNumber(purchaseOrderNumber)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Cart not found"));

        purchaseOrder.setStatus(purchaseOrderStatus);
        purchaseOrderRepository.save(purchaseOrder);
    }

    @Override
    public Optional<List<PurchaseOrderDTO>> findByCustomerId(Long customerId) {
        //TODO: Implement this method
        return Optional.empty();
    }

    @Override
    @Transactional
    public PurchaseOrderDTO save(PurchaseOrderDTO purchaseOrderDTO) {

        if(purchaseOrderDTO.getItems() == null || purchaseOrderDTO.getItems().isEmpty())
            throw new PurchaseOrderItemNotFoundException("PurchaseOrder items not found");

        purchaseOrderDTO.setPurchaseOrderNumber(generateCartNumber());
        purchaseOrderDTO.setStatus(PurchaseOrderStatus.PENDING_PROCESSING);

        List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderDTO.getItems();
        purchaseOrderDTO.setItems(null);

        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrderMapper.purchaseOrderDTOtoPurchaseOrder(purchaseOrderDTO));

        savedPurchaseOrder.setPurchaseOrderItems(new HashSet<>());
        purchaseOrderItems.forEach(item ->{
            item.setPurchaseOrder(savedPurchaseOrder);
            item.setId(new PurchaseOrderItemId(savedPurchaseOrder.getId(), 0));
            savedPurchaseOrder.getPurchaseOrderItems().add(item);
        });

        List<PurchaseOrderItemDTO> purchaseOrderItemDTOList = purchaseOrderItemMapper.purchaseOrderItemSetToPurchaseOrderItemDTOList(savedPurchaseOrder.getPurchaseOrderItems());

        List<PurchaseOrderItemDTO> savedItems = purchaseOrderItemService.saveAll(savedPurchaseOrder.getId(), purchaseOrderItemDTOList);

        BigDecimal total = BigDecimal.ZERO;
        for (PurchaseOrderItemDTO item : savedItems)
            total = total.add(item.getTotalPrice());

        savedPurchaseOrder.setTotalAmount(total);
        PurchaseOrder updated =  purchaseOrderRepository.save(savedPurchaseOrder);

        return purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(updated);
    }



    @Override
    @Transactional
    public PurchaseOrderDTO update(Long id, PurchaseOrderDTO purchaseOrderDTO) {

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Cart not found"));

        PurchaseOrder purchaseOrderWithUpdatedData = purchaseOrderMapper.purchaseOrderDTOtoPurchaseOrder(purchaseOrderDTO);

        purchaseOrder.setPurchaseOrderNumber(purchaseOrderWithUpdatedData.getPurchaseOrderNumber());
        purchaseOrder.setStatus(purchaseOrderWithUpdatedData.getStatus());
        purchaseOrder.setCustomer(purchaseOrderWithUpdatedData.getCustomer());
        purchaseOrder.setPurchaseOrderItems(purchaseOrderWithUpdatedData.getPurchaseOrderItems());
        updateTotalPriceField(purchaseOrderDTO);

        PurchaseOrder updated = purchaseOrderRepository.save(purchaseOrder);

        updated.getPurchaseOrderItems().forEach(item -> item.setPurchaseOrder(purchaseOrder));
        purchaseOrderItemService.updateItemList(id, purchaseOrderItemMapper.purchaseOrderItemSetToPurchaseOrderItemDTOList(updated.getPurchaseOrderItems()));

        return purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(updated);
    }

    @Override
    public List<PurchaseOrderDTO> getAll() {
        return purchaseOrderRepository.findAll().stream()
                .map(purchaseOrderMapper::purchaseOrderToPurchaseOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PurchaseOrderDTO getById(Long id) {
        return purchaseOrderRepository.findById(id)
                .map(purchaseOrderMapper::purchaseOrderToPurchaseOrderDTO)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Cart not found"));
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if (!purchaseOrderRepository.existsById(id))
            throw new PurchaseOrderNotFoundException("Cart not found");

        purchaseOrderRepository.deleteById(id);
    }

    private  void updateTotalPriceField(PurchaseOrder purchaseOrder) {
        updateTotalPriceField(purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(purchaseOrder));
    }

    private static void updateTotalPriceField(PurchaseOrderDTO purchaseOrderDTO) {
        BigDecimal totalPrice = new BigDecimal("0.0");

        for (PurchaseOrderItem item : purchaseOrderDTO.getItems())
            totalPrice = totalPrice.add(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));

        purchaseOrderDTO.setTotalAmount(totalPrice);
    }

    private UUID generateCartNumber() {
        return UUID.randomUUID();
    }

}

