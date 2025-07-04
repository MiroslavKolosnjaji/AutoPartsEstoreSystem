package com.myproject.autopartsestoresystem.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.payments.dto.PaymentMethodDTO;
import com.myproject.autopartsestoresystem.payments.exception.PaymentMethodNotFoundException;
import com.myproject.autopartsestoresystem.payments.entity.PaymentType;
import com.myproject.autopartsestoresystem.payments.service.PaymentMethodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * @author Miroslav Kološnjaji
 */
@WebMvcTest(controllers = {PaymentMethodController.class})
@MockBean(PaymentMethodService.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentMethodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentMethodService paymentMethodService;

    private ObjectMapper objectMapper;

    private PaymentMethodDTO paymentMethodDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        paymentMethodDTO = PaymentMethodDTO.builder().id(1L).paymentType(PaymentType.DEBIT_CARD).build();
    }

    @DisplayName("Get All Payment Methods")
    @Test
    void testGetAllPaymentMethods_whenListIsPopulated_returns200StatusCode() throws Exception {

        //given
        List<PaymentMethodDTO> paymentMethodDTOList = List.of(mock(PaymentMethodDTO.class), mock(PaymentMethodDTO.class));
        when(paymentMethodService.getAll()).thenReturn(paymentMethodDTOList);

        RequestBuilder requestBuilder = get(PaymentMethodController.PAYMENT_METHOD_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(2, objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size());
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        verify(paymentMethodService).getAll();
    }

    @DisplayName("Get All Payment Methods - List Is Empty")
    @Test
    void testGetAllPaymentMethods_whenListIsEmpty_returns404StatusCode() throws Exception {

        //given
        List<PaymentMethodDTO> emptyList = List.of();

        when(paymentMethodService.getAll()).thenReturn(emptyList);

        RequestBuilder requestBuilder = get(PaymentMethodController.PAYMENT_METHOD_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status not match. Expected 404 status code");
        verify(paymentMethodService).getAll();
    }

    @DisplayName("Get Payment Method By ID")
    @Test
    void testGetPaymentMethodById_whenValidIdProvided_returns200StatusCode() throws Exception {

        //given
        when(paymentMethodService.getById(1L)).thenReturn(paymentMethodDTO);

        RequestBuilder requestBuilder = get(PaymentMethodController.PAYMENT_METHOD_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentMethodDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();

        PaymentMethodDTO foundDTO = objectMapper.readValue(response, PaymentMethodDTO.class);

        //then
        assertNotNull(foundDTO, "Payment method should not be null");
        assertEquals(foundDTO.getId(), paymentMethodDTO.getId(), "Payment method id mismatch");
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Status mismatch. Expected Status code 200");

        verify(paymentMethodService).getById(anyLong());

    }

    @DisplayName("Get Payment Method By ID Failed - Invalid ID Provided")
    @Test
    void testGetPaymentMethodByID_whenInvalidMethodProvided_returns404StatusCode() throws Exception {

        //given
        when(paymentMethodService.getById(anyLong())).thenThrow(PaymentMethodNotFoundException.class);

        RequestBuilder requestBuilder = get(PaymentMethodController.PAYMENT_METHOD_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status mismatch. Expected Status code 404");
        verify(paymentMethodService).getById(anyLong());
    }

    @DisplayName("Get Payment Method By Payment Type")
    @Test
    void testGetPaymentMethodByPaymentType_whenValidPaymentTypeProvided_returns200StatusCode() throws Exception {

        //given
        when(paymentMethodService.getByPaymentType(anyString())).thenReturn(paymentMethodDTO);

        RequestBuilder requestBuilder = get(PaymentMethodController.PAYMENT_METHOD_URI + "/payment_type", anyString())
                .param("payment_type", PaymentType.CREDIT_CARD.name())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        PaymentMethodDTO foundDTO = objectMapper.readValue(response, PaymentMethodDTO.class);

        assertNotNull(foundDTO, "Payment method should not be null");
        assertEquals(foundDTO.getId(), paymentMethodDTO.getId(), "Payment method id mismatch");
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Status mismatch. Expected Status code 200");

        verify(paymentMethodService).getByPaymentType(anyString());
    }

    @Test
    void testGetPaymentMethodByPaymentType_whenInvalidPaymentTypeProvided_returns404StatusCode() throws Exception {

        //given
        when(paymentMethodService.getByPaymentType(anyString())).thenThrow(PaymentMethodNotFoundException.class);

        RequestBuilder requestBuilder = get(PaymentMethodController.PAYMENT_METHOD_URI + "/payment_type", anyString())
                .param("payment_type", PaymentType.CREDIT_CARD.name())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Status mismatch. Expected Status code 404");
        verify(paymentMethodService).getByPaymentType(anyString());
    }
}