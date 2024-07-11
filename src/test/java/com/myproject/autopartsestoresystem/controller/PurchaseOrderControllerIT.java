package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.CustomerDTO;
import com.myproject.autopartsestoresystem.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.model.PurchaseOrderItem;
import com.myproject.autopartsestoresystem.model.PurchaseOrderStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PurchaseOrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PurchaseOrderDTO purchaseOrderDTO;

    @BeforeEach
    void setUp() {

        PurchaseOrderItem pi = PurchaseOrderItem.builder()
                .quantity(1)
                .unitPrice(new BigDecimal("100.00"))
                .build();


        purchaseOrderDTO = PurchaseOrderDTO.builder()
                .purchaseOrderNumber(UUID.randomUUID())
                .status(PurchaseOrderStatus.COMPLETED)
                .items(List.of(pi))
                .customer(CustomerDTO.builder().build())
                .totalAmount(new BigDecimal("100.00"))
                .build();
    }

    @Test
    void test() {
    }
}