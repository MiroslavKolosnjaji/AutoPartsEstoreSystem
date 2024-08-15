package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.InvoiceDTO;
import com.myproject.autopartsestoresystem.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.dto.StoreDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.InvoiceCreationException;
import com.myproject.autopartsestoresystem.exception.service.InvoiceNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.PurchaseOrderNotFoundException;
import com.myproject.autopartsestoresystem.mapper.InvoiceMapper;
import com.myproject.autopartsestoresystem.mapper.PurchaseOrderMapper;
import com.myproject.autopartsestoresystem.mapper.StoreMapper;
import com.myproject.autopartsestoresystem.model.Invoice;
import com.myproject.autopartsestoresystem.model.InvoiceItem;
import com.myproject.autopartsestoresystem.repository.InvoiceItemRepository;
import com.myproject.autopartsestoresystem.repository.InvoiceRepository;
import com.myproject.autopartsestoresystem.repository.PurchaseOrderRepository;
import com.myproject.autopartsestoresystem.service.InvoiceService;
import com.myproject.autopartsestoresystem.service.PurchaseOrderService;
import com.myproject.autopartsestoresystem.service.StoreService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final PurchaseOrderService purchaseOrderService;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final StoreService storeService;
    private final StoreMapper storeMapper;
    private final InvoiceItemRepository invoiceItemRepository;

    @Override
    public void createInvoice(Long purchaseOrderId, Long storeId) throws InvoiceCreationException {

        try {
            PurchaseOrderDTO purchaseOrderDTO = purchaseOrderService.getById(purchaseOrderId);
            StoreDTO storeDTO = storeService.getById(storeId);

            Invoice invoice = Invoice.builder()
                    .purchaseOrder(purchaseOrderMapper.purchaseOrderDTOtoPurchaseOrder(purchaseOrderDTO))
                    .store(storeMapper.storeDTOToStore(storeDTO))
                    .invoiceItems(new LinkedList<>())
                    .build();

            List<InvoiceItem> invoiceItems = new LinkedList<>();
            purchaseOrderDTO.getItems().forEach(item -> {
                InvoiceItem invoiceItem = InvoiceItem.builder()
                        .productName(item.getPart().getPartName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .discountPercentage(BigDecimal.ZERO)
                        .taxPercentage(BigDecimal.ZERO)
                        .build();

                invoiceItems.add(invoiceItem);

            });

            invoice.setInvoiceItems(invoiceItems);
            invoice.setTotalAmount(calculateTotalPrice(invoiceItems));

            Invoice savedInvoice = invoiceRepository.save(invoice);
            invoiceItemRepository.saveAll(savedInvoice.getInvoiceItems());

        } catch (EntityNotFoundException e) {
            throw new InvoiceCreationException(e);
        }

    }

    @Override
    public InvoiceDTO getInvoice(Long id) throws InvoiceNotFoundException {
        return invoiceRepository.findById(id)
                .map(invoiceMapper::invoiceToInvoiceDTO)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));
    }

    private BigDecimal calculateTotalPrice(List<InvoiceItem> invoiceItems) {
        return invoiceItems.stream()
                .map(InvoiceItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
