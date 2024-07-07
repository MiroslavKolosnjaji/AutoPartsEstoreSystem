package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.PaymentMethodDTO;
import com.myproject.autopartsestoresystem.model.PaymentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentMethodControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentMethodDTO paymentMethodDTO;

    @BeforeEach
    void setUp() {
        paymentMethodDTO = PaymentMethodDTO.builder().paymentType(PaymentType.CASH).build();
    }

    @Test
    void testGetAllPaymentMethods_whenListIsPopulated_returns200StatusCode() throws Exception {

        mockMvc.perform(get(PaymentMethodController.PAYMENT_METHOD_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }


    @Test
    void testGetPaymentMethodById_whenValidIdProvided_returns200StatusCode() throws Exception {

        mockMvc.perform(get(PaymentMethodController.PAYMENT_METHOD_URI_WITH_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    void testGetPaymentMethodById_whenInvalidIdProvided_returns400StatusCode() throws Exception {

        mockMvc.perform(get(PaymentMethodController.PAYMENT_METHOD_URI_WITH_ID, 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPaymentMethodByPaymentMethodType_whenValidPaymentMethodTypeRProvided_returns200StatusCode() throws Exception {

        mockMvc.perform(get(PaymentMethodController.PAYMENT_METHOD_URI + "/payment_type")
                        .param("payment_type", "CREDIT_CARD")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"));

    }
}