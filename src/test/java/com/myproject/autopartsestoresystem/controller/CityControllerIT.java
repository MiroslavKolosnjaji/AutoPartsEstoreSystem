package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.customer.CityDTO;
import com.myproject.autopartsestoresystem.model.City;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CityControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("Create City")
    @Order(3)
    @Test
    void testCreateCity_whenValidDetailsProvided_returns201StatusCode() throws Exception {

        CityDTO cityDTO = getTestCityDTO();

        mockMvc.perform(post(CityController.CITY_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", CityController.CITY_URI + "/4"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(cityDTO.getName()))
                .andExpect(jsonPath("$.zipCode").value(cityDTO.getZipCode()));
    }

    @DisplayName("Create City Failed - Invalid Details Provided - Returns Code 400")
    @Order(99)
    @Test
    void testCreateCity_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        CityDTO cityDTO = getTestCityDTO();
        cityDTO.setZipCode("1234");

        mockMvc.perform(post(CityController.CITY_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Update City")
    @Order(4)
    @Test
    void testUpdateCity_whenValidDetailsProvided_returns204StatusCode() throws Exception {
        CityDTO cityDTO = getTestCityDTO();
        cityDTO.setId(4L);
        cityDTO.setZipCode("22222");

        mockMvc.perform(put(CityController.CITY_URI_WITH_ID, cityDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Get City By ID")
    @Order(1)
    @Test
    void testGetCityById_whenValidIdProvided_returnsCityDTO() throws Exception {
        mockMvc.perform(get(CityController.CITY_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Get City By ID Failed - Invalid ID Provided - Returns Code 404")
    @Test
    void testGetCityById_whenInvalidIdProvided_returns4040StatusCode() throws Exception {
        mockMvc.perform(get(CityController.CITY_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get All Cities")
    @Order(2)
    @Test
    void testGetCities_whenListIsPopulated_returnsListOfCityDto() throws Exception {
        mockMvc.perform(get(CityController.CITY_URI)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Delete City")
    @Order(5)
    @Test
    void testDeleteCity_whenValidIdProvided_returns204StatusCode() throws Exception {

        mockMvc.perform(delete(CityController.CITY_URI_WITH_ID, 4)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Customer Failed - Invalid ID provided - Returns 404 Status Code")
    @Test
    void testDeleteCustomer_whenInvalidIdProvided_returns404StatusCode() throws Exception {
        mockMvc.perform(delete(CustomerController.CUSTOMER_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private CityDTO getTestCityDTO() {
        return CityDTO.builder()
                .name("New York")
                .zipCode("11111")
                .build();
    }
}