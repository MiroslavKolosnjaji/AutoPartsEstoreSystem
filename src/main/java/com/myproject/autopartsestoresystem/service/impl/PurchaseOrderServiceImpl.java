package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.exception.service.CartNotFoundException;
import com.myproject.autopartsestoresystem.mapper.PurchaseOrderItemMapper;
import com.myproject.autopartsestoresystem.mapper.PurchaseOrderMapper;
import com.myproject.autopartsestoresystem.model.PurchaseOrder;
import com.myproject.autopartsestoresystem.model.PurchaseOrderStatus;
import com.myproject.autopartsestoresystem.model.PurchaseOrderItem;
import com.myproject.autopartsestoresystem.repository.PurchaseOrderRepository;
import com.myproject.autopartsestoresystem.service.PurchaseOrderItemService;
import com.myproject.autopartsestoresystem.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
    public PurchaseOrderDTO findByCartNumber(UUID cartNumber) {

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByPurchaseOrderNumber(cartNumber)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        return purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(purchaseOrder);
    }


    @Override
    public void updateCartStatus(UUID cartNumber, PurchaseOrderStatus purchaseOrderStatus) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByPurchaseOrderNumber(cartNumber)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

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

        purchaseOrderDTO.setPurchaseOrderNumber(generateCartNumber());
        purchaseOrderDTO.setStatus(PurchaseOrderStatus.PENDING_PROCESSING);

        PurchaseOrder purchaseOrder = purchaseOrderMapper.purchaseOrderDTOtoPurchaseOrder(purchaseOrderDTO);

        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        savedPurchaseOrder.getPurchaseOrderItems().forEach(item -> item.setPurchaseOrder(savedPurchaseOrder));
        purchaseOrderItemService.saveAll(purchaseOrderItemMapper.purchaseOrderItemSetToPurchaseOrderDTOList(savedPurchaseOrder.getPurchaseOrderItems()));


        return purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(savedPurchaseOrder);
    }

    @Override
    @Transactional
    public PurchaseOrderDTO update(Long id, PurchaseOrderDTO purchaseOrderDTO) {

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        PurchaseOrder purchaseOrderWithUpdatedData = purchaseOrderMapper.purchaseOrderDTOtoPurchaseOrder(purchaseOrderDTO);

        purchaseOrder.setPurchaseOrderNumber(purchaseOrderWithUpdatedData.getPurchaseOrderNumber());
        purchaseOrder.setStatus(purchaseOrderWithUpdatedData.getStatus());
        purchaseOrder.setCustomer(purchaseOrderWithUpdatedData.getCustomer());
        purchaseOrder.setPurchaseOrderItems(purchaseOrderWithUpdatedData.getPurchaseOrderItems());

        PurchaseOrder updated = purchaseOrderRepository.save(purchaseOrder);

        updated.getPurchaseOrderItems().forEach(item -> item.setPurchaseOrder(purchaseOrder));
        purchaseOrderItemService.updateItemList(id, purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(updated).getItems());
        
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
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if (!purchaseOrderRepository.existsById(id))
            throw new CartNotFoundException("Cart not found");

        purchaseOrderRepository.deleteById(id);
    }

    private UUID generateCartNumber() {
        return UUID.randomUUID();
    }

    private void addOrdinalNumberToItem(PurchaseOrder purchaseOrder) {
        int itemCounter = 1;
        for (PurchaseOrderItem purchaseOrderItem : purchaseOrder.getPurchaseOrderItems()) {
            purchaseOrderItem.getId().setOrdinalNumber(itemCounter++);
        }
    }

}

