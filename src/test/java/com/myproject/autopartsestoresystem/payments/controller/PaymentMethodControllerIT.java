package com.myproject.autopartsestoresystem.payments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.util.BaseIT;
import com.myproject.autopartsestoresystem.payments.dto.PaymentMethodDTO;
import com.myproject.autopartsestoresystem.payments.entity.PaymentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentMethodControllerIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PaymentMethodDTO paymentMethodDTO;

    @BeforeEach
    void setUp() {
        paymentMethodDTO = PaymentMethodDTO.builder().paymentType(PaymentType.CASH).build();
    }

    @DisplayName("Get All Payment Methods")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testGetAllPaymentMethods_whenListIsPopulated_returns200StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(PaymentMethodController.PAYMENT_METHOD_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentMethodDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @DisplayName("Get All Payment Methods Failed - User Role - Return Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testGetAllPaymentMethods_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(PaymentMethodController.PAYMENT_METHOD_URI)
                .with(httpBasic(user, password))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Get All Payment Methods Failed - No Role - Returns Code 401")
    @Test
    void testGetAllPaymentMethods_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(PaymentMethodController.PAYMENT_METHOD_URI)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get Payment Method By ID")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testGetPaymentMethodById_whenValidIdProvided_returns200StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(PaymentMethodController.PAYMENT_METHOD_URI_WITH_ID, 1L)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentMethodDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.id").value(1));

    }

    @DisplayName("Get Payment Method By ID Failed - Invalid ID Provided - Returns Code 400")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testGetPaymentMethodById_whenInvalidIdProvided_returns400StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(PaymentMethodController.PAYMENT_METHOD_URI_WITH_ID, 99L)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get Payment Method By ID Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testGetPaymentMethodByID_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(PaymentMethodController.PAYMENT_METHOD_URI_WITH_ID, 1L)
                .with(httpBasic(user, password))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }
    @DisplayName("Get Payment Method By ID Failed - No Role - Returns Code 401")
    @Test
    void testGetPaymentByID_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(PaymentMethodController.PAYMENT_METHOD_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    //    @Test
//    void testGetPaymentMethodByPaymentMethodType_whenValidPaymentMethodTypeRProvided_returns200StatusCode() throws Exception {
//
//        mockMvc.perform(get(PaymentMethodController.PAYMENT_METHOD_URI + "/payment_type")
//                        .param("payment_type", PaymentType.CASH)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(header().string("Content-type", "application/json"));
//
//    }
}