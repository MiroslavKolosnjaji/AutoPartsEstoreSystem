package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.CustomerDTO;
import com.myproject.autopartsestoresystem.dto.PartDTO;
import com.myproject.autopartsestoresystem.dto.PurchaseOrderDTO;
import com.myproject.autopartsestoresystem.mapper.PartMapper;
import com.myproject.autopartsestoresystem.model.Part;
import com.myproject.autopartsestoresystem.model.PurchaseOrderItem;
import com.myproject.autopartsestoresystem.service.CustomerService;
import com.myproject.autopartsestoresystem.service.PartService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PartService partService;

    @Autowired
    private PartMapper partMapper;

    private PurchaseOrderDTO purchaseOrderDTO;

    @BeforeEach
    void setUp() {

        CustomerDTO customerDTO = customerService.getById(1L);
        PartDTO partDTO = partService.getById(1L);

        if(partDTO.getPrices() == null || partDTO.getPrices().isEmpty())
            throw new IllegalArgumentException("No part prices found");

        Part part = partMapper.partDTOToPart(partDTO);



        PurchaseOrderItem purchaseOrderItem = PurchaseOrderItem.builder()
                .part(part)
                .quantity(2)
                .build();


        List<PurchaseOrderItem> purchaseOrderItems = Collections.singletonList(purchaseOrderItem);

        purchaseOrderDTO = PurchaseOrderDTO.builder()
                .customer(customerDTO)
                .items(purchaseOrderItems)
                .build();
    }

}