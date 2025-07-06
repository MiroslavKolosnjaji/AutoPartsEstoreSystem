package com.myproject.autopartsestoresystem.parts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myproject.autopartsestoresystem.parts.dto.PartDTO;
import com.myproject.autopartsestoresystem.parts.entity.*;
import com.myproject.autopartsestoresystem.parts.exception.PartNotFoundException;
import com.myproject.autopartsestoresystem.parts.service.PartService;
import com.myproject.autopartsestoresystem.parts.service.PriceService;
import org.junit.jupiter.api.BeforeAll;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @author Miroslav Kološnjaji
 */
@WebMvcTest(controllers = PartController.class)
@MockBean({PartService.class, PriceService.class})
@AutoConfigureMockMvc(addFilters = false)
class PartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartService partService;

    @Autowired
    private PriceService priceService;

    private static ObjectMapper objectMapper;

    private PartDTO partDTO;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {

        List<Price> prices = new ArrayList<>();
        prices.add(Price.builder().id(new PriceId(1L, 0L)).price(new BigDecimal("129.99")).currency(Currency.USD).dateModified(LocalDateTime.now()).build());

        partDTO = PartDTO.builder().partNumber("BP12345")
                .partName("Front Brake Pad Set")
                .description("High-Performance brake pads for superior stopping power. Suitable for both everyday driving and high-performance use.")
                .partGroup(PartGroup.builder().id(1L).name(PartGroupType.BRAKING_SYSTEM).parts(new ArrayList<>()).build())
                .prices(prices)
                .vehicles(new ArrayList<>())
                .build();
    }

    @DisplayName("Create Part")
    @Test
    void testCreatePart_whenValidDetailsProvided_returnsCreatedPartDTO() throws Exception {

        //given
        when(partService.save(any(PartDTO.class))).thenReturn(partDTO);

        RequestBuilder requestBuilder = post(PartController.PART_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        PartDTO savedPartDTO = objectMapper.readValue(response, PartDTO.class);

        //then
        assertAll("Saved Part Validation",
                () -> assertEquals(partDTO.getPartNumber(), savedPartDTO.getPartNumber(), "Part number doens't match"),
                () -> assertEquals(partDTO.getPartName(), savedPartDTO.getPartName(), "Name doesn't match"),
                () -> assertEquals(partDTO.getPartGroup(), savedPartDTO.getPartGroup(), "Part group doesn't match"),
                () -> assertEquals(partDTO.getDescription(), savedPartDTO.getDescription(), "Description doesn't match"),
                () -> assertEquals(partDTO.getPrices(), savedPartDTO.getPrices(), "Price list doesn't match")
        );

        verify(partService).save(any(PartDTO.class));
    }

    @DisplayName("Create Part When Invalid Details Provided - Returns Status 400")
    @Test
    void testCreatePart_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {


        //given
        partDTO.setPartNumber("");

        RequestBuilder requestBuilder = post(PartController.PART_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");

    }

    @DisplayName("Update Part")
    @Test
    void testUpdatePart_whenUpdatingCurrentPriceWithValidDetailsProvided_returns204StatusCode() throws Exception {

        //given
        partDTO.setId(1L);
        partDTO.getPrices().get(0).setPrice(new BigDecimal("599.99"));

        when(partService.update(partDTO.getId(), partDTO)).thenReturn(partDTO);

        RequestBuilder requestBuilder = put(PartController.PART_URI_WITH_ID, partDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(partService).update(anyLong(), any(PartDTO.class));
    }

    @DisplayName("Update Part When Invalid Input Provided - Returns Status 400")
    @Test
    void testUpdatePart_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        //given
        partDTO.setId(1L);
        partDTO.setPartGroup(null);


        RequestBuilder requestBuilder = put(PartController.PART_URI_WITH_ID, partDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Get All Parts")
    @Test
    void testGetAllParts_whenListIsPopulated_returnsListOfPartDTO() throws Exception {

        //given
        List<PartDTO> parts = new ArrayList<>();
        parts.add(partDTO);
        partDTO.setId(1L);

        when(partService.getAll()).thenReturn(parts);

        RequestBuilder requestBuilder = get(PartController.PART_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(1, objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size(), "Expected 1 part to be returned");
        verify(partService).getAll();
    }

    @DisplayName("Get Part By ID")
    @Test
    void testGetPartById_whenValidIdProvided_returnsPartDTO() throws Exception {

        //given
        partDTO.setId(1L);
        when(partService.getById(1L)).thenReturn(partDTO);

        RequestBuilder requestBuilder = get(PartController.PART_URI_WITH_ID, partDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        PartDTO foundDTO = objectMapper.readValue(response, PartDTO.class);


        //then
        assertAll("Saved Part Validation",
                () -> assertEquals(partDTO.getId(), foundDTO.getId(), "ID doesn't match"),
                () -> assertEquals(partDTO.getPartNumber(), foundDTO.getPartNumber(), "Part number doens't match"),
                () -> assertEquals(partDTO.getPartName(), foundDTO.getPartName(), "Name doesn't match"),
                () -> assertEquals(partDTO.getPartGroup(), foundDTO.getPartGroup(), "Part group doesn't match"),
                () -> assertEquals(partDTO.getDescription(), foundDTO.getDescription(), "Description doesn't match"),
                () -> assertEquals(partDTO.getPrices(), foundDTO.getPrices(), "Price list doesn't match")
        );
        verify(partService).getById(1L);
    }

    @DisplayName("Get Part By ID When Invalid ID Provided - Returns 404 Status Code")
    @Test
    void testGetPartById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        when(partService.getById(anyLong())).thenThrow(PartNotFoundException.class);

        RequestBuilder requestBuilder = get(PartController.PART_URI_WITH_ID, 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        verify(partService).getById(anyLong());

    }

    @DisplayName("Delete Part")
    @Test
    void testDeleteModel_whenValidIdProvided_returns204StatusCode() throws Exception {

        //given
        doNothing().when(partService).delete(anyLong());

        RequestBuilder requestBuilder = delete(PartController.PART_URI_WITH_ID, 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(partService).delete(anyLong());

    }

    @DisplayName("Delete Part When Invalid ID Provided - Returns 404 Status Code")
    @Test
    void testDeleteModel_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        final String MESSAGE = "Part not found";
        doThrow(new PartNotFoundException(MESSAGE)).when(partService).delete(anyLong());

        RequestBuilder requestBuilder = delete(PartController.PART_URI_WITH_ID, 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        assertTrue(response.contains(MESSAGE), "Message doesn't match");
        verify(partService).delete(anyLong());

    }
}