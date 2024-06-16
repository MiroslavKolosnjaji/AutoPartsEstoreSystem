package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.customer.CityDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.CityNotFoundException;
import com.myproject.autopartsestoresystem.model.City;
import com.myproject.autopartsestoresystem.service.CityService;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@WebMvcTest(value = CityController.class)
@MockBean({CityService.class})
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CityService cityService;

    ObjectMapper objectMapper;

    CityDTO cityDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        cityDTO = CityDTO.builder()
                .name("Belgrade")
                .zipCode("11000")
                .build();
    }

    @DisplayName("Create City - Successful")
    @Test
    void testCreateCity_whenValidCityDetailsProvided_returnsCreatedCityDTO() throws Exception {

        //given
        when(cityService.save(any(CityDTO.class))).thenReturn(cityDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(CityController.CITY_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        CityDTO savedDTO = objectMapper.readValue(response, CityDTO.class);


        assertAll("Saved City Validation",
                () -> assertEquals(cityDTO.getName(), savedDTO.getName(), "City name doesn't match"),
                () -> assertEquals(cityDTO.getZipCode(), savedDTO.getZipCode(), "ZipCode doesn't match"));

        verify(cityService).save(any(CityDTO.class));
    }

    @DisplayName("Create City With Invalid City Details Provided - Returns Status 400 ")
    @Test
    void testCreateCity_whenCityNameIsNotProvided_returns400StatusCode() throws Exception {

        //given
        cityDTO.setName("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(CityController.CITY_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Update City - Successful")
    @Test
    void testUpdateCity_whenValidCityDetailsProvided_returns204StatusCode() throws Exception {

        //given
        cityDTO.setId(1L);
        cityDTO.setName("Kragujevac");
        when(cityService.update(any(Long.class), any(CityDTO.class))).thenReturn(cityDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(CityController.CITY_URI_WITH_ID, cityDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(cityService).update(any(Long.class), any(CityDTO.class));
    }

    @DisplayName("Update City With Invalid City Details Provided - Return Status 400")
    @Test
    void testUpdateCity_whenInvalidCityDetailsProvided_returns400StatusCode() throws Exception {

        //given
        cityDTO.setId(1L);
        cityDTO.setName("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(CityController.CITY_URI_WITH_ID, cityDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Get All Cities")
    @Test
    void testGetCities_whenListIsPopulated_returnsTwoCities() throws Exception {

        //given
        List<CityDTO> cities = getCities();
        when(cityService.getAll()).thenReturn(cities);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CityController.CITY_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cities));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(2, objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size(), "Expected 2 cities returned");
        verify(cityService).getAll();
    }

    @DisplayName("Get City When Valid ID Provided")
    @Test
    void testGetCity_whenValidIdProvided_returnsCityDTOWithId1() throws Exception {

        //given
        cityDTO.setId(1L);
        when(cityService.getById(any(Long.class))).thenReturn(cityDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CityController.CITY_URI_WITH_ID, cityDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        CityDTO getCityDTO = objectMapper.readValue(response, CityDTO.class);

        //then
        assertAll("Get City by id validation",
                () -> assertEquals(cityDTO.getId(), getCityDTO.getId(), "City id doesn't match"),
                () -> assertEquals(cityDTO.getName(), getCityDTO.getName(), "City name doesn't match"),
                () -> assertEquals(cityDTO.getZipCode(), getCityDTO.getZipCode(), "ZipCode doesn't match"));
    }

    @DisplayName("Get City When Invalid ID Provided - Tell me. Returns 404 Status Code")
    @Test
    void testGetCity_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        Long id = 100L;
        when(cityService.getById(any(Long.class))).thenThrow(CityNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(CityController.CITY_URI_WITH_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");

    }

    @DisplayName("Delete City -Successful")
    @Test
    void testDeleteCity_whenValidIdProvided_returns204StatusCode() throws Exception {
        //given
        Long id = 1L;
        doNothing().when(cityService).delete(id);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(CityController.CITY_URI_WITH_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
    }


    @DisplayName("Delete City Invalid ID provided - Returns 404 Status Code")
    @Test
    void testDeleteCity_whenInvalidIdProvided_throwsCityDoesntExistsAndReturns404StatusCode() throws Exception {

        //given
        final String MESSAGE = "City doesn't found";
        Long invalidId = 100L;
        doThrow(new CityNotFoundException(MESSAGE)).when(cityService).delete(invalidId);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(CityController.CITY_URI_WITH_ID, invalidId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBody = result.getResponse().getContentAsString();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        assertTrue(responseBody.contains(MESSAGE), "Error message doesn't match");
        verify(cityService).delete(invalidId);
    }

    private List<CityDTO> getCities() {
        return Arrays.asList(cityDTO, CityDTO.builder().name("Kragujevac").zipCode("34000").build());
    }
}