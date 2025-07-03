package com.myproject.autopartsestoresystem.orders.service.impl;

import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrder;
import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrderItem;
import com.myproject.autopartsestoresystem.orders.entity.PurchaseOrderItemId;
import com.myproject.autopartsestoresystem.parts.dto.PartDTO;
import com.myproject.autopartsestoresystem.orders.dto.PurchaseOrderItemDTO;
import com.myproject.autopartsestoresystem.orders.exception.PurchaseOrderItemNotFoundException;
import com.myproject.autopartsestoresystem.orders.mapper.PurchaseOrderItemMapper;
import com.myproject.autopartsestoresystem.model.Currency;
import com.myproject.autopartsestoresystem.parts.entity.Part;
import com.myproject.autopartsestoresystem.parts.entity.Price;
import com.myproject.autopartsestoresystem.orders.repository.PurchaseOrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class PurchaseOrderItemServiceImplTest {

    @Mock
    private PurchaseOrderItemRepository repository;

    @Mock
    PurchaseOrderItemMapper purchaseOrderItemMapper;

    @InjectMocks
    private PurchaseOrderItemServiceImpl purchaseOrderItemService;

    private PurchaseOrderItemDTO purchaseOrderItemDTO;
    private PurchaseOrderItem purchaseOrderItem;

    @BeforeEach
    void setUp() {
        purchaseOrderItemDTO = PurchaseOrderItemDTO.builder()
                .purchaseOrder(PurchaseOrder.builder().build())
                .purchaseOrderId(2L)
                .part(PartDTO.builder()
                        .id(1L)
                        .partNumber("1231231")
                        .prices(List.of(Price.builder().price(new BigDecimal("123.99")).currency(Currency.USD).build()))
                        .build())
                .quantity(3)
                .unitPrice(new BigDecimal("124.99"))
                .build();

        purchaseOrderItem = PurchaseOrderItem.builder()
                .purchaseOrder(PurchaseOrder.builder().build())
                .id(PurchaseOrderItemId.builder().purchaseOrderId(1L).ordinalNumber(1).build())
                .part(Part.builder()
                        .id(1L)
                        .partNumber("1231231")
                        .prices(List.of(Price.builder().price(new BigDecimal("123.99")).currency(Currency.USD).build()))
                        .build())
                .unitPrice(new BigDecimal("123.99"))
                .totalPrice(new BigDecimal("0"))
                .build();

    }

    @DisplayName("Save Purchase Order Item")
    @Test
    void testSaveOrderItem_whenValidDetailsProvided_thenReturnsOrderItemDTO() {

        //given
        PurchaseOrderItem purchaseOrderItem = mock(PurchaseOrderItem.class);
        when(purchaseOrderItemMapper.purchaseOrderItemDTOToPurchaseOrderItem(purchaseOrderItemDTO)).thenReturn(purchaseOrderItem);
        when(purchaseOrderItemMapper.purchaseOrderItemToPurchaseOrderItemDTO(purchaseOrderItem)).thenReturn(purchaseOrderItemDTO);
        when(repository.save(purchaseOrderItem)).thenReturn(purchaseOrderItem);

        //when
        PurchaseOrderItemDTO savedDTO = purchaseOrderItemService.save(purchaseOrderItemDTO);

        //then
        assertNotNull(savedDTO, "Saved DTO should not be null");
        assertEquals(purchaseOrderItemDTO, savedDTO, "Saved DTO should be equal");

        verify(purchaseOrderItemMapper).purchaseOrderItemDTOToPurchaseOrderItem(purchaseOrderItemDTO);
        verify(purchaseOrderItemMapper).purchaseOrderItemToPurchaseOrderItemDTO(purchaseOrderItem);
        verify(repository).save(purchaseOrderItem);
    }

    @DisplayName("Save All Purchase Order Items")
    @Test
    void testSaveAllPurchaseOrderItems_whenValidDetailsProvided_returnsListOfPurchaseOrderItemDTO() {

        //given
        List<PurchaseOrderItemDTO> purchaseOrderItemDTOS = List.of(purchaseOrderItemDTO);
        List<PurchaseOrderItem> purchaseOrderItems = List.of(mock(PurchaseOrderItem.class));

        when(purchaseOrderItemMapper.purchaseOrderItemDTOListToPurchaseOrderItemList(purchaseOrderItemDTOS)).thenReturn(purchaseOrderItems);
        when(purchaseOrderItemMapper.purchaseOrderItemListToPurchaseOrderDTOList(purchaseOrderItems)).thenReturn(purchaseOrderItemDTOS);
        when(repository.saveAll(purchaseOrderItems)).thenReturn(purchaseOrderItems);

        //when

        List<PurchaseOrderItemDTO>  savedDTOs = purchaseOrderItemService.saveAll(anyLong(), purchaseOrderItemDTOS);

        //then
        assertNotNull(savedDTOs);
        assertEquals(purchaseOrderItemDTOS, savedDTOs, "Saved DTO should be equal");

        verify(purchaseOrderItemMapper).purchaseOrderItemDTOListToPurchaseOrderItemList(purchaseOrderItemDTOS);
        verify(purchaseOrderItemMapper).purchaseOrderItemListToPurchaseOrderDTOList(purchaseOrderItems);
        verify(repository).saveAll(purchaseOrderItems);
    }

    @Test
    void testUpdateAllPurchaseOrderItems_whenValidDetailsProvided_returnsListOfPurchaseOrderItem() throws PurchaseOrderItemNotFoundException {

        //given
        Set<PurchaseOrderItem> purchaseOrderItemSet  = new HashSet<>();
        purchaseOrderItemSet.add(purchaseOrderItem);

        List<PurchaseOrderItem> purchaseOrderItemList = List.of(purchaseOrderItem);
        List<PurchaseOrderItemDTO> purchaseOrderItemDTOS = List.of(purchaseOrderItemDTO);

        when(repository.findByPurchaseOrderId(anyLong())).thenReturn(Optional.of(purchaseOrderItemSet));
        when(repository.saveAll(purchaseOrderItemSet)).thenReturn(purchaseOrderItemList);
        when(purchaseOrderItemMapper.purchaseOrderItemListToPurchaseOrderDTOList(purchaseOrderItemList)).thenReturn(purchaseOrderItemDTOS);

        //when
        List<PurchaseOrderItemDTO> updatedList = purchaseOrderItemService.updateItemList(anyLong(), purchaseOrderItemDTOS);

        //then
        assertNotNull(updatedList);
        assertEquals(purchaseOrderItemDTOS, updatedList, "Updated DTO should be equal");

        verify(repository).saveAll(purchaseOrderItemSet);
        verify(repository).findByPurchaseOrderId(anyLong());
        verify(purchaseOrderItemMapper).purchaseOrderItemListToPurchaseOrderDTOList(purchaseOrderItemList);
    }

    @Test
    void testGetAllPurchaseOrderItems_whenListIsNotEmpty_returnsListOfPurchaseOrderDTO() {

        //given
        List<PurchaseOrderItem> purchaseOrderItemList = List.of(mock(PurchaseOrderItem.class));
        when(repository.findAll()).thenReturn(purchaseOrderItemList);

        //when
        List<PurchaseOrderItemDTO> purchaseOrderItemDTOS = purchaseOrderItemService.getAll();

        //then
        assertNotNull(purchaseOrderItemDTOS);
        assertEquals(1, purchaseOrderItemDTOS.size());

        verify(repository).findAll();
    }

    @Test
    void testGetPurchaseOrderItemById_whenValidIdProvided_returnsPurchaseOrderItemDTO() throws PurchaseOrderItemNotFoundException {

        //given
        PurchaseOrderItemId id = PurchaseOrderItemId.builder().purchaseOrderId(2L).ordinalNumber(1).build();
        when(repository.findById(id)).thenReturn(Optional.of(mock(PurchaseOrderItem.class)));
        when(purchaseOrderItemMapper.purchaseOrderItemToPurchaseOrderItemDTO(any(PurchaseOrderItem.class))).thenReturn(purchaseOrderItemDTO);

        //when
        PurchaseOrderItemDTO purchaseOrderItemDTO = purchaseOrderItemService.getById(id);

        //then
        assertNotNull(purchaseOrderItemDTO);

        verify(repository).findById(id);
        verify(purchaseOrderItemMapper).purchaseOrderItemToPurchaseOrderItemDTO(any(PurchaseOrderItem.class));

    }
}