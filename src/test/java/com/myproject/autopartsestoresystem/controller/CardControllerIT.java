package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.CardDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CardControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CardDTO cardDTO;


    @BeforeEach
    void setUp() {

        cardDTO = CardDTO.builder()
                .cardHolder("John Smith")
                .cardNumber("5286244935876258")
                .expiryDate(LocalDate.of(2025,12, 1))
                .cvv("123")
                .customerId(1L)
                .build();
    }

    @DisplayName("Create Card")
    @Order(1)
    @Test
    void createCard_whenValidDetailsProvided_returns201StatusCode() throws Exception {

        mockMvc.perform(post(CardController.CARD_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", CardController.CARD_URI + "/2"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cardHolder").value("John Smith"))
                .andExpect(jsonPath("$.expiryDate").value("2025-12-01"));

    }

    @DisplayName("Create Card Failed - Invalid Details Provided")
    @Test
    void testCreateCard_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        cardDTO.setCardNumber("12345");

        mockMvc.perform(post(CardController.CARD_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Update Card")
    @Order(2)
    @Test
    void testUpdateCard_WhenValidDetailsProvided_returns204StatusCode() throws Exception {

        cardDTO.setCardHolder("John Thompson");
        cardDTO.setId(2L);

        mockMvc.perform(put(CardController.CARD_URI_WITH_ID, 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Update Card Failed - Invalid Details Provided")
    @Test
    void testUpdateCard_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        cardDTO.setCardNumber("12345");

        mockMvc.perform(put(CardController.CARD_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Get Card By ID")
    @Order(3)
    @Test
    void testGetCardById_whenValidIdProvided_returns200StatusCode() throws Exception {

        mockMvc.perform(get(CardController.CARD_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.cardHolder").value("John Smith"));
    }

    @DisplayName("Get Card By ID Failed - Invalid ID Provided")
    @Test
    void testGetCardById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        mockMvc.perform(get(CardController.CARD_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get Card By Holder Name")
    @Order(5)
    @Test
    void testGetCardByHolderName_whenValidHolderNameProvided_returns204StatusCode() throws Exception {

        mockMvc.perform(get(CardController.CARD_URI + "/holderName").param("holderName","John Smith")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardHolder").value("John Smith"));
    }

    @DisplayName("Delete Card")
    @Order(4)
    @Test
    void testDeleteCard_whenValidIdProvided_returns204StatusCode() throws Exception {

        mockMvc.perform(delete(CardController.CARD_URI_WITH_ID, 2L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        
    }

    @DisplayName("Delete Card Failed - Ivalid ID Provided")
    @Test
    void testDeleteCard_whenIvalidIdProvided_returns404StatusCode() throws Exception {

        mockMvc.perform(delete(CardController.CARD_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}