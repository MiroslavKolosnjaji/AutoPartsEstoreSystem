package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.InvoiceDTO;
import com.myproject.autopartsestoresystem.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.dto.StoreDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.InvoiceCreationException;
import com.myproject.autopartsestoresystem.exception.service.InvoiceNotFoundException;
import com.myproject.autopartsestoresystem.mapper.InvoiceItemMapper;
import com.myproject.autopartsestoresystem.mapper.InvoiceMapper;
import com.myproject.autopartsestoresystem.mapper.PurchaseOrderMapper;
import com.myproject.autopartsestoresystem.mapper.StoreMapper;
import com.myproject.autopartsestoresystem.model.*;
import com.myproject.autopartsestoresystem.repository.InvoiceItemRepository;
import com.myproject.autopartsestoresystem.repository.InvoiceRepository;
import com.myproject.autopartsestoresystem.service.PurchaseOrderService;
import com.myproject.autopartsestoresystem.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceMapper invoiceMapper;

    @Mock
    private InvoiceItemRepository invoiceItemRepository;

    @Mock
    private PurchaseOrderService purchaseOrderService;

    @Mock
    private PurchaseOrderMapper purchaseOrderMapper;

    @Mock
    private StoreService storeService;

    @Mock
    private StoreMapper storeMapper;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private Invoice invoice;

    @BeforeEach
    void setUp() {

        Part part = Part.builder()
                .id(1L)
                .partNumber("12345")
                .partName("Test Part")
                .description("Test Description")
                .prices(List.of(Price.builder().price(new BigDecimal("134.99")).currency(Currency.USD).build()))
                .build();


        PurchaseOrderItem purchaseOrderItem = PurchaseOrderItem.builder()
                .part(part)
                .quantity(2)
                .unitPrice(new BigDecimal("134.99"))
                .totalPrice(new BigDecimal("134.99"))
                .build();

        invoice = Invoice.builder()
                .store(Store.builder().id(1L).build())
                .purchaseOrder(PurchaseOrder.builder().id(1L).purchaseOrderItems(Set.of(purchaseOrderItem)).build())
                .invoiceItems(new LinkedList<>())
                .totalAmount(new BigDecimal("99.99"))
                .build();


    }

    @DisplayName("Create Invoice")
    @Test
    void testCreateInvoice_whenValidIDsProvided_thenInvoiceCreated() throws EntityNotFoundException, InvoiceCreationException {

        //given
        List<InvoiceItem> invoiceItemList = List.of(mock(InvoiceItem.class));
        PurchaseOrderDTO purchaseOrderDTO = mock(PurchaseOrderDTO.class);
        PurchaseOrder purchaseOrder = mock(PurchaseOrder.class);
        StoreDTO storeDTO = mock(StoreDTO.class);
        Store store = mock(Store.class);
        Long purchaseOrderId = 1L;
        Long storeId = 1L;

        when(purchaseOrderService.getById(purchaseOrderId)).thenReturn(purchaseOrderDTO);
        when(storeService.getById(storeId)).thenReturn(storeDTO);
        when(purchaseOrderMapper.purchaseOrderDTOtoPurchaseOrder(purchaseOrderDTO)).thenReturn(purchaseOrder);
        when(storeMapper.storeDTOToStore(storeDTO)).thenReturn(store);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        when(invoiceItemRepository.saveAll(invoice.getInvoiceItems())).thenReturn(invoiceItemList);

        //when
        invoiceService.createInvoice(storeId, purchaseOrderId);

        //then
        verify(purchaseOrderService).getById(purchaseOrderId);
        verify(storeService).getById(storeId);
        verify(invoiceRepository).save(any(Invoice.class));
        verify(invoiceItemRepository).saveAll(invoice.getInvoiceItems());
    }

    @DisplayName("Create Invoice Failed - Purchase Order Not Found")
    @Test
    void testCreateInvoice_whenPurchaseOrderNotFound_throwsInvoiceCreationException() throws EntityNotFoundException {

        //given
        when(purchaseOrderService.getById(anyLong())).thenThrow(EntityNotFoundException.class);

        //when
        Executable executable = () -> invoiceService.createInvoice(1L, 1L);

        //then
        assertThrows(InvoiceCreationException.class, executable, "Exception doesn't match. InvoiceCreationException expected");
        verify(purchaseOrderService).getById(anyLong());

    }

    @DisplayName("Create Invoice Failed - Store Not Found")
    @Test
    void testCreateInvoice_whenStoreNotFound_throwsInvoiceCreationException() throws EntityNotFoundException {

        //given
        when(storeService.getById(anyLong())).thenThrow(EntityNotFoundException.class);

        //when
        Executable executable = () -> invoiceService.createInvoice(1L, 1L);

        //then
        assertThrows(InvoiceCreationException.class, executable, "Exception doesn't match. InvoiceCreationException expected");
        verify(storeService).getById(anyLong());
    }

    @DisplayName("Get Invoice")
    @Test
    void testGetInvoice_whenValidIdProvided_returnsInvoiceDTO() throws InvoiceNotFoundException {

        //given
        InvoiceDTO invoiceDTO = mock(InvoiceDTO.class);

        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.ofNullable(invoice));
        when(invoiceMapper.invoiceToInvoiceDTO(invoice)).thenReturn(invoiceDTO);

        //when
        InvoiceDTO foundDTO = invoiceService.getInvoice(anyLong());

        //then
        assertNotNull(foundDTO);
        verify(invoiceRepository).findById(anyLong());
        verify(invoiceMapper).invoiceToInvoiceDTO(invoice);
    }

    @DisplayName("Get Invoice Failed - Invalid ID")
    @Test
    void testGetInvoice_whenInvalidIDProvided_throwsInvoiceNotFoundException(){

        //given
        when(invoiceRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> invoiceService.getInvoice(anyLong());

        //then
        assertThrows(InvoiceNotFoundException.class, executable, "Exception doesn't match. InvoiceNotFoundException expected");
    }
}