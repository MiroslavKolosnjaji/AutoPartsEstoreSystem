package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myproject.autopartsestoresystem.dto.CardDTO;
import com.myproject.autopartsestoresystem.exception.service.CardNotFoundException;
import com.myproject.autopartsestoresystem.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @author Miroslav Kološnjaji
 */
@WebMvcTest(controllers = CardController.class)
@MockBean({CardService.class})
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardService cardService;

    private ObjectMapper objectMapper;
    private CardDTO cardDTO;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());


        cardDTO = CardDTO.builder()
                .cardHolder("John Smith")
                .cardNumber("5528637386855499")
                .expiryDate(LocalDate.of(2025,12, 1))
                .cvv("123")
                .customerId(1L)
                .build();
    }

    @Disabled("Create Card")
    @Test
    void testCreateCard_whenValidDetailsProvided_returnsCreatedCardDTO() throws Exception {

        //given
        when(cardService.save(any(CardDTO.class))).thenReturn(cardDTO);

        RequestBuilder requestBuilder = post(CardController.CARD_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        CardDTO savedDTO = objectMapper.readValue(response, CardDTO.class);

        //then
        assertAll("Saved Card Validation",
                () -> assertEquals(cardDTO.getCardNumber(), savedDTO.getCardNumber(), "Card Number doesn't match"),
                () -> assertEquals(cardDTO.getCardHolder(), savedDTO.getCardHolder(), "Card Holder doesn't match"),
                () -> assertEquals(cardDTO.getCvv(), savedDTO.getCvv(), "CVV doesn't match"),
                () -> assertEquals(cardDTO.getExpiryDate(), savedDTO.getExpiryDate(), "Expiry date doesn't match"));

        verify(cardService).save(any(CardDTO.class));
    }

    @DisplayName("Create Card Failed - Invalid Details Provided")
    @Test
    void testCreateCard_whenIvalidDetailsProvided_returns400StatusCode() throws Exception {

        //given
        cardDTO.setCardNumber("123123");

        RequestBuilder requestBuilder = post(CardController.CARD_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Update Card")
    @Test
    void testUpdateCard_whenValidIdProvided_returns204StatusCode() throws Exception {

        //given
        cardDTO.setCardHolder("John Thompson");
        when(cardService.update(anyLong(), any(CardDTO.class))).thenReturn(cardDTO);

        RequestBuilder requestBuilder = put(CardController.CARD_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(cardService).update(anyLong(), any(CardDTO.class));
    }

    @DisplayName("Update Card Failed - Invalid Details Provided")
    @Test
    void testUpdateCard_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        //given
        cardDTO.setCardNumber("123123");

        RequestBuilder requestBuilder = put(CardController.CARD_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Get Cards By Holder Name")
    @Test
    void testGetCardByHolderName_whenValidHolderNameProvided_returnsListOfCardDTO() throws Exception {

        //given
        List<CardDTO> cards = List.of(mock(CardDTO.class), mock(CardDTO.class));
        when(cardService.getCardsByHolderName(anyString())).thenReturn(cards);

        RequestBuilder requestBuilder = get(CardController.CARD_URI + "/holderName").param("holderName", "John Smith")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cards));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(2, objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size(), "Incorrect number of Cards returned, expected 2");
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Incorrect status code returned, expected 200 expected");

        verify(cardService).getCardsByHolderName(anyString());
    }

    @DisplayName("Get Card By Holder Name - Empty List")
    @Test
    void testGetCardByHolderName_whenHolderDoesntHaveAnyCard_returns204StatusCode() throws Exception {

        //given
        List<CardDTO> cards = List.of();
        when(cardService.getCardsByHolderName(anyString())).thenReturn(cards);

        RequestBuilder requestBuilder = get(CardController.CARD_URI + "/holderName").param("holderName", "John Smith")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cards));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(cardService).getCardsByHolderName(anyString());
    }

    @DisplayName("Get Card By ID")
    @Test
    void testGetCardById_whenValidIdProvided_returnsCardDTO() throws Exception{

        //given
        when(cardService.getById(anyLong())).thenReturn(cardDTO);

        RequestBuilder requestBuilder = get(CardController.CARD_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cardDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        CardDTO foundDTO = objectMapper.readValue(response, CardDTO.class);

        //then
        assertEquals(cardDTO, foundDTO, "Found DTO should be equal");
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 200 expected");
        verify(cardService).getById(anyLong());

    }

    @Test
    void testGetCardById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        when(cardService.getById(anyLong())).thenThrow(CardNotFoundException.class);

        RequestBuilder requestBuilder = get(CardController.CARD_URI_WITH_ID, 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        verify(cardService).getById(anyLong());
    }

    @Test
    void testDeleteCardById_whenValidIdProvided_returns204StatusCode() throws Exception {

        //given
        doNothing().when(cardService).delete(1L);

        RequestBuilder requestBuilder = delete(CardController.CARD_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

         //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 200 expected");
        verify(cardService).delete(1L);
    }

    @Test
    void testDeleteCardById_whenInvalidIdProvided_returns() throws Exception {

        //given
        doThrow(CardNotFoundException.class).when(cardService).delete(anyLong());

        RequestBuilder requestBuilder = delete(CardController.CARD_URI_WITH_ID, 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        verify(cardService).delete(anyLong());
    }
}