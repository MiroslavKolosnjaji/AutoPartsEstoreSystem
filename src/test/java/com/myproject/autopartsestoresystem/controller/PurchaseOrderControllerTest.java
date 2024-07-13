package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.CustomerDTO;
import com.myproject.autopartsestoresystem.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.exception.service.PurchaseOrderNotFoundException;
import com.myproject.autopartsestoresystem.model.Part;
import com.myproject.autopartsestoresystem.model.PurchaseOrderItem;
import com.myproject.autopartsestoresystem.model.PurchaseOrderStatus;
import com.myproject.autopartsestoresystem.service.PurchaseOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @author Miroslav Kološnjaji
 */
@WebMvcTest(controllers = PurchaseOrderController.class)
@MockBean(PurchaseOrderService.class)
class PurchaseOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    private ObjectMapper objectMapper;
    private PurchaseOrderDTO purchaseOrderDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        Part part = Part.builder()
                .id(1L)
                .partNumber("12345")
                .partName("Test Part")
                .description("Test Description")
                .prices(new ArrayList<>())
                .build();


        PurchaseOrderItem purchaseOrderItem = PurchaseOrderItem.builder()
                .part(part)
                .quantity(2)
                .build();


        purchaseOrderDTO = PurchaseOrderDTO.builder()
                .id(1L)
                .purchaseOrderNumber(UUID.randomUUID())
                .totalAmount(new BigDecimal("122.99"))
                .status(PurchaseOrderStatus.PENDING_PROCESSING)
                .customer(CustomerDTO.builder().build())
                .items(List.of(purchaseOrderItem))
                .build();

    }

    //TODO: FIX THIS TEST METHOD
//    @DisplayName("Create Purchase Order")
//    @Test
//    void testCreatePurchaseOrder_whenValidDetailsProvided_returns201StatusCode() throws Exception {
//
//        //given
//        when(purchaseOrderService.save(purchaseOrderDTO)).thenReturn(purchaseOrderDTO);
//
//        RequestBuilder requestBuilder = post(PurchaseOrderController.PURCHASE_ORDER_URI)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(purchaseOrderDTO));
//
//        //when
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        String responseBody = result.getResponse().getContentAsString();
//        PurchaseOrderDTO savedDTO = objectMapper.readValue(responseBody, PurchaseOrderDTO.class);
//
//
//       assertNotNull(savedDTO, "Saved DTO should not be null");
//
//       assertAll("Purchase order fields validation",
//               () -> assertEquals(purchaseOrderDTO.getPurchaseOrderNumber(), savedDTO.getPurchaseOrderNumber(), "Purchase order number mismatch"),
//               () -> assertEquals(purchaseOrderDTO.getStatus(), savedDTO.getStatus(), "Purchase order status mismatch"),
//               () -> assertEquals(purchaseOrderDTO.getCustomer(), savedDTO.getCustomer(), "Purchase order customer mismatch"),
//               () -> assertEquals(purchaseOrderDTO.getItems(), savedDTO.getItems(), "Purchase order items mismatch"));
//
//        verify(purchaseOrderService).save(purchaseOrderDTO);
//    }

    @DisplayName("Create PurchaseOrder Failed - Invalid Details Provided")
    @Test
    void testCreatePurchaseOrder_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        //given
        purchaseOrderDTO.setItems(null);

        RequestBuilder requestBuilder = post(PurchaseOrderController.PURCHASE_ORDER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseOrderDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Update Purchase Order")
    @Test
    void testUpdatePurchaseOrder_whenValidDetailsProvided_returns204StatusCode() throws Exception {

        //given
        when(purchaseOrderService.update(anyLong(), any(PurchaseOrderDTO.class))).thenReturn(purchaseOrderDTO);

        RequestBuilder requestBuilder = put(PurchaseOrderController.PURCHASE_ORDER_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseOrderDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(purchaseOrderService).update(anyLong(), any(PurchaseOrderDTO.class));
    }

    @DisplayName("Update Purchase Order Failed - Invalid Details Provided")
    @Test
    void testUpdatePurchaseOrder_whenInvalidDetailsProvided_return400StatusCode() throws Exception {

        //given
        purchaseOrderDTO.setItems(null);

        RequestBuilder requestBuilder = put(PurchaseOrderController.PURCHASE_ORDER_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseOrderDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Get All Purchase Orders")
    @Test
    void testGetAllPurchaseOrders_whenListIsPopulated_returns200StatusCode() throws Exception {

        //given
        List<PurchaseOrderDTO> purchaseOrderDTOList = List.of(mock(PurchaseOrderDTO.class), mock(PurchaseOrderDTO.class));
        when(purchaseOrderService.getAll()).thenReturn(purchaseOrderDTOList);

        RequestBuilder requestBuilder = get(PurchaseOrderController.PURCHASE_ORDER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseOrderDTOList));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(2, objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size(), "Expected 2 purchase orders");
        verify(purchaseOrderService).getAll();
    }

    @DisplayName("Get Purchase Order By ID")
    @Test
    void testGetPurchaseById_whenValidIdProvided_returns200StatusCode() throws Exception {

        //given
        when(purchaseOrderService.getById(anyLong())).thenReturn(purchaseOrderDTO);

        RequestBuilder requestBuilder = get(PurchaseOrderController.PURCHASE_ORDER_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(purchaseOrderDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        PurchaseOrderDTO foundDTO = objectMapper.readValue(response, PurchaseOrderDTO.class);

        //then
        assertEquals(purchaseOrderDTO.getId(), foundDTO.getId(), "FoundDTO mismatch");
        verify(purchaseOrderService).getById(anyLong());
    }

    @DisplayName("Get Purchase Order By ID Failed - Invalid ID Provided")
    @Test
    void testGetPurchaseOrderById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        when(purchaseOrderService.getById(anyLong())).thenThrow(PurchaseOrderNotFoundException.class);

        RequestBuilder requestBuilder = get(PurchaseOrderController.PURCHASE_ORDER_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
    }

    @DisplayName("Delete Purchase Order By ID")
    @Test
    void testDeletePurchaseOrderById_whenValidIdPprovided_returns204StatusCode() throws Exception {

        //when
        doNothing().when(purchaseOrderService).delete(anyLong());

        RequestBuilder requestBuilder = delete(PurchaseOrderController.PURCHASE_ORDER_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(purchaseOrderService).delete(anyLong());
    }

    @DisplayName("Delete Purchase Order By ID Failed - Invalid ID Provided")
    @Test
    void testDeletePurchaseOrderById_whenInvalidIdProvided_return404StatusCode() throws Exception {

        //given
        doThrow(PurchaseOrderNotFoundException.class).when(purchaseOrderService).delete(anyLong());

        RequestBuilder requestBuilder = delete(PurchaseOrderController.PURCHASE_ORDER_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        verify(purchaseOrderService).delete(anyLong());
    }
}