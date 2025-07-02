package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.customers.dto.CustomerDTO;
import com.myproject.autopartsestoresystem.customers.entity.Customer;
import com.myproject.autopartsestoresystem.parts.dto.PaymentDTO;
import com.myproject.autopartsestoresystem.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.dto.PurchaseOrderItemDTO;
import com.myproject.autopartsestoresystem.exception.service.PaymentProcessingException;
import com.myproject.autopartsestoresystem.exception.service.PurchaseOrderItemNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.PurchaseOrderNotFoundException;
import com.myproject.autopartsestoresystem.mapper.PurchaseOrderItemMapper;
import com.myproject.autopartsestoresystem.mapper.PurchaseOrderMapper;
import com.myproject.autopartsestoresystem.model.*;
import com.myproject.autopartsestoresystem.model.Currency;
import com.myproject.autopartsestoresystem.parts.entity.Part;
import com.myproject.autopartsestoresystem.parts.entity.Price;
import com.myproject.autopartsestoresystem.repository.PurchaseOrderRepository;
import com.myproject.autopartsestoresystem.service.PaymentService;
import com.myproject.autopartsestoresystem.service.PurchaseOrderItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class PurchaseOrderServiceImplTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private PurchaseOrderMapper purchaseOrderMapper;

    @Mock
    private PurchaseOrderItemMapper purchaseOrderItemMapper;

    @Mock
    private PurchaseOrderItemService purchaseOrderItemService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PurchaseOrderServiceImpl purchaseOrderService;

    private PurchaseOrderDTO purchaseOrderDTO;

    private PurchaseOrderItemDTO purchaseOrderItemDTO;

    private PurchaseOrder purchaseOrder;

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

        purchaseOrderItemDTO = PurchaseOrderItemDTO.builder()
                .purchaseOrderId(1L)
                .purchaseOrder(new PurchaseOrder())
                .quantity(2)
                .unitPrice(new BigDecimal("134.99"))
                .totalPrice(new BigDecimal("268.98"))
                .build();

        purchaseOrderDTO = PurchaseOrderDTO.builder()
                .id(1L)
                .purchaseOrderNumber(UUID.randomUUID())
                .totalAmount(new BigDecimal("268.98"))
                .status(PurchaseOrderStatus.PENDING_PROCESSING)
                .customer(CustomerDTO.builder().build())
                .items(List.of(purchaseOrderItem))
                .build();

        purchaseOrder = PurchaseOrder.builder()
                .id(1L)
                .purchaseOrderNumber(purchaseOrderDTO.getPurchaseOrderNumber())
                .totalAmount(purchaseOrderDTO.getTotalAmount())
                .status(PurchaseOrderStatus.PENDING_PROCESSING)
                .customer(Customer.builder().build())
                .purchaseOrderItems(Set.of(purchaseOrderItem))
                .build();

    }


    @DisplayName("Save Purchase Order")
    @Test
    void testSavePurchaseOrder_whenValidDetailsProvided_returnsPurchaseOrderDTO() throws PaymentProcessingException, PurchaseOrderItemNotFoundException {

        //given
        List<PurchaseOrderItemDTO> purchaseOrderItems = List.of(purchaseOrderItemDTO);
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .paymentMethod(PaymentMethod.builder().paymentType(purchaseOrderDTO.getPaymentType()).build())
                .card(null)
                .amount(purchaseOrder.getTotalAmount())
                .status(PaymentStatus.PROCESSING)
                .build();

        when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);
        when(purchaseOrderMapper.purchaseOrderDTOtoPurchaseOrder(purchaseOrderDTO)).thenReturn(purchaseOrder);
        when(purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(purchaseOrder)).thenReturn(purchaseOrderDTO);
        when(purchaseOrderItemMapper.purchaseOrderItemSetToPurchaseOrderItemDTOList(anySet())).thenReturn(purchaseOrderItems);
        when(purchaseOrderItemService.saveAll(purchaseOrder.getId(), purchaseOrderItems)).thenReturn(purchaseOrderItems);
        when(paymentService.save(paymentDTO)).thenReturn(paymentDTO);


        //when
        PurchaseOrderDTO savedDTO = purchaseOrderService.savePurchaseOrder(purchaseOrderDTO);

        //then
        assertNotNull(savedDTO);
        assertEquals(purchaseOrderDTO, savedDTO);

        verify(purchaseOrderRepository, times(2)).save(purchaseOrder);
        verify(purchaseOrderMapper).purchaseOrderDTOtoPurchaseOrder(purchaseOrderDTO);
        verify(purchaseOrderMapper).purchaseOrderToPurchaseOrderDTO(purchaseOrder);
        verify(purchaseOrderItemMapper).purchaseOrderItemSetToPurchaseOrderItemDTOList(anySet());
        verify(purchaseOrderItemService).saveAll(purchaseOrder.getId(), purchaseOrderItems);
        verify(paymentService).save(paymentDTO);
    }

    @DisplayName("Update Purchase Order")
    @Test
    void testUpdatePurchaseOrder_whenValidDetailsProvided_returnsUpdatedPurchaseOrderDTO() throws PurchaseOrderItemNotFoundException, PurchaseOrderNotFoundException {

        //given
        List<PurchaseOrderItemDTO> purchaseOrderItems = List.of(purchaseOrderItemDTO);

        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(purchaseOrder));
        when(purchaseOrderMapper.purchaseOrderDTOtoPurchaseOrder(purchaseOrderDTO)).thenReturn(purchaseOrder);
        when(purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(purchaseOrder)).thenReturn(purchaseOrderDTO);
        when(purchaseOrderItemMapper.purchaseOrderItemSetToPurchaseOrderItemDTOList(anySet())).thenReturn(purchaseOrderItems);
        when(purchaseOrderItemService.updateItemList(1L, purchaseOrderItems)).thenReturn(purchaseOrderItems);

        when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder);

        //when
        PurchaseOrderDTO updatedDTO = purchaseOrderService.update(1L, purchaseOrderDTO);

        assertNotNull(updatedDTO);
        assertEquals(purchaseOrderDTO, updatedDTO);

        verify(purchaseOrderRepository).findById(anyLong());
        verify(purchaseOrderMapper).purchaseOrderToPurchaseOrderDTO(purchaseOrder);
        verify(purchaseOrderMapper).purchaseOrderDTOtoPurchaseOrder(purchaseOrderDTO);
        verify(purchaseOrderItemMapper).purchaseOrderItemSetToPurchaseOrderItemDTOList(anySet());
        verify(purchaseOrderItemService).updateItemList(1L, purchaseOrderItems);
        verify(purchaseOrderRepository).save(purchaseOrder);
    }

    @DisplayName("Update Purchase Order Failed - Invalid ID Provided")
    @Test
    void testUpdatePurchaseOrder_whenInvalidIDProvided_throwsPurchaseOrderNotFoundException() {

        //given
        when(purchaseOrderRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> purchaseOrderService.update(anyLong(), purchaseOrderDTO);

        //then
        assertThrows(PurchaseOrderNotFoundException.class, executable, "Exception not match. Expected PurchaseOrderNotFoundException");
        verify(purchaseOrderRepository).findById(anyLong());
    }

    @DisplayName("Get All Purchase Orders")
    @Test
    void testGetAllPurchaseOrders_whenListIsNotEmpty_returnsListOfPurchaseOrderDTO() {

        //given
        List<PurchaseOrder> purchaseOrders = List.of(purchaseOrder);
        when(purchaseOrderRepository.findAll()).thenReturn(purchaseOrders);

        //when
        List<PurchaseOrderDTO> allPurchaseOrderDTOS = purchaseOrderService.getAll();

        //then
        assertNotNull(allPurchaseOrderDTOS);
        assertEquals(1, allPurchaseOrderDTOS.size());

        verify(purchaseOrderRepository).findAll();
    }

    //TODO: FIX THIS TEST METHOD
//    @DisplayName("Get Purchase Order By ID")
//    @Test
//    void testGetPurchaseOrderById_whenValidIdProvided_thenReturnPurchaseOrderDTO() {
//
//        //given
//        when(purchaseOrderRepository.findById(purchaseOrder.getId())).thenReturn(Optional.of(purchaseOrder));
//        when(purchaseOrderMapper.purchaseOrderToPurchaseOrderDTO(purchaseOrder)).thenReturn(purchaseOrderDTO);
//
//        //when
//        PurchaseOrderDTO foundPurchaseOrder = purchaseOrderService.getById(purchaseOrder.getId());
//
//        //then
//        assertNotNull(foundPurchaseOrder);
//        assertAll("Validate FoundDTO fields",
//                () -> assertEquals(purchaseOrder.getId(), foundPurchaseOrder.getId()),
//                () -> assertEquals(purchaseOrder.getStatus(), foundPurchaseOrder.getStatus()),
//                () -> assertEquals(purchaseOrder.getTotalAmount(), foundPurchaseOrder.getTotalAmount()));
//
//        verify(purchaseOrderRepository).findById(purchaseOrder.getId());
//        verify(purchaseOrderMapper).purchaseOrderDTOtoPurchaseOrder(purchaseOrderDTO);
//    }

    @DisplayName("Get Purchase Order By ID Failed - Invalid ID Provided")
    @Test
    void testGetPurchaseOrderById_whenInvalidIdProvided_throwsPurchaseOrderNotFoundException() {

        //given
        when(purchaseOrderRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> purchaseOrderService.getById(anyLong());

        //then
        assertThrows(PurchaseOrderNotFoundException.class, executable, "Exception not match. Expected PurchaseOrderNotFoundException");
        verify(purchaseOrderRepository).findById(anyLong());
    }

    @DisplayName("Delete Purchase Order By ID")
    @Test
    void testDeletePurchaseOrderByID_whenValidIDProvided_thenCorrect() throws PurchaseOrderNotFoundException {

        //given
        when(purchaseOrderRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(purchaseOrderRepository).deleteById(anyLong());

        //when
        purchaseOrderService.delete(anyLong());

        //then
        verify(purchaseOrderRepository).deleteById(anyLong());
    }

    @DisplayName("Delete Purchase Order By ID Failed - Invalid ID Provided")
    @Test
    void testDeletePurchaseOrderByID_whenInvalidIDProvided_throwsPurchaseOrderNotFoundException() {

        //given
        when(purchaseOrderRepository.existsById(anyLong())).thenReturn(false);

        //when
        Executable executable = () -> purchaseOrderService.delete(anyLong());

        //then
        assertThrows(PurchaseOrderNotFoundException.class, executable, "Exception not match. Expected PurchaseOrderNotFoundException");
        verify(purchaseOrderRepository).existsById(anyLong());
    }
}