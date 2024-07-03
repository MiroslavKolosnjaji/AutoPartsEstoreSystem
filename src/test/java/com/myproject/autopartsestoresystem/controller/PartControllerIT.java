package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.PartDTO;
import com.myproject.autopartsestoresystem.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PartControllerIT {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PartDTO partDTO;

    @BeforeEach
    void setUp() {

        List<Price> prices = new ArrayList<>();
        prices.add(Price.builder().id(new PriceId(4L, 0L)).price(new BigDecimal("79.99")).currency(Currency.USD).build());

        partDTO = PartDTO.builder()
                .partNumber("ABS67890")
                .partName("Wheel Speed Sensor")
                .partGroup(PartGroup.builder().id(1L).name(PartGroupType.BRAKING_SYSTEM).build())
                .description("Sensor that monitors the wheel speed and provides data to the ABS system to prevent wheel lock-up during braking." +
                        "Essential for maintaining control during sudden stops.")
                .prices(prices)
                .vehicles(new ArrayList<>())
                .build();
    }

    @DisplayName("Create Part")
    @Order(3)
    @Test
    void testCreatePart_whenValidDetailsProvided_returns201StatusCode() throws Exception {

        Price price = partDTO.getPrices().get(0);

        mockMvc.perform(post(PartController.PART_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", PartController.PART_URI + "/4"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.partNumber").value(partDTO.getPartNumber()))
                .andExpect(jsonPath("$.partName").value(partDTO.getPartName()))
                .andExpect(jsonPath("$.description").value(partDTO.getDescription()))
                .andExpect(jsonPath("$.partGroup").value(partDTO.getPartGroup()))
                .andExpect(jsonPath("$.prices", hasSize(1)))
                .andExpect(jsonPath("$.prices[0].id.partId").value(price.getId().getPartId()))
                .andExpect(jsonPath("$.prices[0].id.priceId").value(price.getId().getPriceId()))
                .andExpect(jsonPath("$.prices[0].price").value(price.getPrice()))
                .andExpect(jsonPath("$.prices[0].currency").value(price.getCurrency().name()));
    }

    @DisplayName("Create Part Failed - Invalid Details Provided - Returns Code 400")
    @Test
    void testCreatePart_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        partDTO.setPartGroup(null);

        mockMvc.perform(post(PartController.PART_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Update Part - Current Price Not Changed")
    @Order(4)
    @Test
    void testUpdatePart_whenValidDetailsProvidedAndPriceNotChanged_returns204StatusCode() throws Exception {

        partDTO.setId(4L);
        partDTO.setPartNumber("ABS67891");

        mockMvc.perform(put(PartController.PART_URI_WITH_ID, partDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partDTO)))
                .andExpect(status().isNoContent());

    }

    @DisplayName("Update Part - Update Current Price")
    @Order(6)
    @Test
    void testUpdatePart_whenUpdatingCurrentPrice_returns204StatusCode() throws Exception {

        partDTO.setId(4L);
        partDTO.getPrices().get(partDTO.getPrices().size() - 1).setPrice(new BigDecimal("389.99"));

        mockMvc.perform(put(PartController.PART_URI_WITH_ID, partDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partDTO)))
                .andExpect(status().isNoContent());


    }

    @DisplayName("Update Part - Add New Price")
    @Order(5)
    @ParameterizedTest
    @CsvFileSource(resources = "/newPrice.csv", numLinesToSkip = 1)
    void testUpdatePart_whenAddingNewPrice_returns204StatusCode(BigDecimal newPrice, Long id) throws Exception {

        partDTO.setId(4L);
        partDTO.getPrices().add(Price.builder().id(new PriceId(partDTO.getId(), id)).currency(Currency.USD).price(newPrice).build());

        mockMvc.perform(put(PartController.PART_URI_WITH_ID, partDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partDTO)))
                .andExpect(status().isNoContent());


    }

    @DisplayName("Get Part By ID")
    @Order(1)
    @Test
    void testGetPartById_whenValidIdProvided_returnsPartDTO() throws Exception {

        mockMvc.perform(get(PartController.PART_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Get Part By ID Failed - Invalid ID Provided - Returns Code 404")
    @Test
    void testGetPartById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        mockMvc.perform(get(PartController.PART_URI_WITH_ID, 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get All Parts")
    @Test
    void testGetAllParts_whenListIsPopulated_returnsListOfPartDTO() throws Exception {

        mockMvc.perform(get(PartController.PART_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Delete Part")
    @Order(7)
    @Test
    void testDeletePart_whenValidIdProvided_returns204StatusCode() throws Exception {

        mockMvc.perform(delete(PartController.PART_URI_WITH_ID, 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Part Failed - Ivalid ID Provided - Returns 404 Status Code")
    @Test
    void testDeletePart_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        mockMvc.perform(delete(PartController.PART_URI_WITH_ID, 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}