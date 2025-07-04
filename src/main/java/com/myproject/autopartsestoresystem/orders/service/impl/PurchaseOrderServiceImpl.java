package com.myproject.autopartsestoresystem.orders.service.impl;

import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrder;
import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrderItem;
import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrderItemId;
import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrderStatus;
import com.myproject.autopartsestoresystem.orders.service.PurchaseOrderItemService;
import com.myproject.autopartsestoresystem.orders.service.PurchaseOrderService;
import com.myproject.autopartsestoresystem.parts.dto.PaymentDTO;
import com.myproject.autopartsestoresystem.orders.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.orders.dto.PurchaseOrderItemDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.payments.entity.PaymentMethod;
import com.myproject.autopartsestoresystem.payments.entity.PaymentStatus;
import com.myproject.autopartsestoresystem.payments.exception.PaymentProcessingException;
import com.myproject.autopartsestoresystem.orders.exception.PurchaseOrderNotFoundException;
import com.myproject.autopartsestoresystem.orders.exception.PurchaseOrderItemNotFoundException;
import com.myproject.autopartsestoresystem.orders.mapper.PurchaseOrderItemMapper;
import com.myproject.autopartsestoresystem.orders.mapper.PurchaseOrderMapper;
import com.myproject.autopartsestoresystem.orders.repository.PurchaseOrderRepository;
import com.myproject.autopartsestoresystem.payments.service.PaymentService;
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
    private final PaymentService paymentService;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PurchaseOrderItemMapper purchaseOrderItemMapper;


    @Override
    public PurchaseOrderDTO findByPurchaseOrderNumber(UUID purchaseOrderNumber) throws PurchaseOrderNotFoundException {

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByPurchaseOrderNumber(purchaseOrderNumber)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("Cart not found"));

        return purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(purchaseOrder);
    }


    @Override
    public void updateOrderStatus(UUID purchaseOrderNumber, PurchaseOrderStatus purchaseOrderStatus) throws PurchaseOrderNotFoundException {
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
    public PurchaseOrderDTO save(PurchaseOrderDTO entity) throws EntityAlreadyExistsException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    @Transactional
    public PurchaseOrderDTO savePurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws PurchaseOrderItemNotFoundException, PaymentProcessingException {

        if (purchaseOrderDTO.getItems() == null || purchaseOrderDTO.getItems().isEmpty())
            throw new PurchaseOrderItemNotFoundException("PurchaseOrder items not found");

        purchaseOrderDTO.setPurchaseOrderNumber(generatePurchaseOrderNumber());
        purchaseOrderDTO.setStatus(PurchaseOrderStatus.PENDING_PROCESSING);

        List<PurchaseOrderItem> purchaseOrderItems = purchaseOrderDTO.getItems();
        purchaseOrderDTO.setItems(null);

        PurchaseOrder savedPurchaseOrder = purchaseOrderRepository.save(purchaseOrderMapper.purchaseOrderDTOtoPurchaseOrder(purchaseOrderDTO));

        savedPurchaseOrder.setPurchaseOrderItems(new HashSet<>());
        purchaseOrderItems.forEach(item -> {
            item.setPurchaseOrder(savedPurchaseOrder);
            item.setId(new PurchaseOrderItemId(savedPurchaseOrder.getId(), 0));
            savedPurchaseOrder.getPurchaseOrderItems().add(item);
        });

        List<PurchaseOrderItemDTO> purchaseOrderItemDTOList = purchaseOrderItemMapper.purchaseOrderItemSetToPurchaseOrderItemDTOList(savedPurchaseOrder.getPurchaseOrderItems());

        List<PurchaseOrderItemDTO> savedItems = purchaseOrderItemService.saveAll(savedPurchaseOrder.getId(), purchaseOrderItemDTOList);

        savedPurchaseOrder.setTotalAmount(getTotal(savedItems));

        PaymentDTO paymentDTO = PaymentDTO.builder()
                .paymentMethod(PaymentMethod.builder().paymentType(purchaseOrderDTO.getPaymentType()).build())
                .card(null)
                .amount(savedPurchaseOrder.getTotalAmount())
                .status(PaymentStatus.PROCESSING)
                .build();


        if (purchaseOrderDTO.getCustomer().getCards() != null && !purchaseOrderDTO.getCustomer().getCards().isEmpty()) {
            paymentDTO.setCard(purchaseOrderDTO.getCustomer().getCards().get(0));
            paymentService.save(purchaseOrderDTO.getPaymentToken(), paymentDTO);
        } else {
            paymentService.save(paymentDTO);
        }

        savedPurchaseOrder.setStatus(PurchaseOrderStatus.COMPLETED);

        PurchaseOrder updated = purchaseOrderRepository.save(savedPurchaseOrder);

        return purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(updated);
    }

    private BigDecimal getTotal(List<PurchaseOrderItemDTO> purchaseOrderItemDTOList) {
        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseOrderItemDTO item : purchaseOrderItemDTOList)
            total = total.add(item.getTotalPrice());

        return total;
    }


    @Override
    @Transactional
    public PurchaseOrderDTO update(Long id, PurchaseOrderDTO purchaseOrderDTO) throws PurchaseOrderNotFoundException, PurchaseOrderItemNotFoundException {

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
    public PurchaseOrderDTO getById(Long id) throws PurchaseOrderNotFoundException {
        return purchaseOrderRepository.findById(id)
                .map(purchaseOrderMapper::purchaseOrderToPurchaseOrderDTO)
                .orElseThrow(() -> new PurchaseOrderNotFoundException("PurchaseOrder not found"));
    }

    @Override
    @Transactional
    public void delete(Long id) throws PurchaseOrderNotFoundException {

        if (!purchaseOrderRepository.existsById(id))
            throw new PurchaseOrderNotFoundException("PurchaseOrder not found");

        purchaseOrderRepository.deleteById(id);
    }

    private void updateTotalPriceField(PurchaseOrder purchaseOrder) {
        updateTotalPriceField(purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(purchaseOrder));
    }

    private static void updateTotalPriceField(PurchaseOrderDTO purchaseOrderDTO) {
        BigDecimal totalPrice = new BigDecimal("0.0");

        for (PurchaseOrderItem item : purchaseOrderDTO.getItems())
            totalPrice = totalPrice.add(item.getUnitPrice().multiply(new BigDecimal(item.getQuantity())));

        purchaseOrderDTO.setTotalAmount(totalPrice);
    }

    private UUID generatePurchaseOrderNumber() {
        return UUID.randomUUID();
    }

}

