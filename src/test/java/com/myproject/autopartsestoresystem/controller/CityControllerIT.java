package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.CityDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CityControllerIT extends BaseIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("Create City")
    @Order(3)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreateCity_whenValidDetailsProvided_returns201StatusCode(String user, String password) throws Exception {

        CityDTO cityDTO = getTestCityDTO();

        mockMvc.perform(post(CityController.CITY_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", CityController.CITY_URI + "/4"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(cityDTO.getName()))
                .andExpect(jsonPath("$.zipCode").value(cityDTO.getZipCode()));
    }

    @DisplayName("Create City Failed - Invalid Details Provided")
    @Order(99)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testCreateCity_whenInvalidDetailsProvided_returns400StatusCode(String user, String password) throws Exception {

        CityDTO cityDTO = getTestCityDTO();
        cityDTO.setZipCode("1234");

        mockMvc.perform(post(CityController.CITY_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Create City Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testCreateCity_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        CityDTO cityDTO = getTestCityDTO();
        cityDTO.setName("ABC");
        

        mockMvc.perform(post(CityController.CITY_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Create City Failed - No Role - Returns Code 401")
    @Test
    void testCreateCity_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(post(CityController.CITY_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Update City")
    @Order(4)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testUpdateCity_whenValidDetailsProvided_returns204StatusCode(String user, String password) throws Exception {
        CityDTO cityDTO = getTestCityDTO();
        cityDTO.setId(4L);
        cityDTO.setZipCode("22222");

        mockMvc.perform(put(CityController.CITY_URI_WITH_ID, cityDTO.getId())
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Update City Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testUpdateCity_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        CityDTO cityDTO = getTestCityDTO();
        cityDTO.setId(4L);
        cityDTO.setZipCode("22222");

        mockMvc.perform(put(CityController.CITY_URI_WITH_ID, cityDTO.getId())
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityDTO)))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Update City Failed - No Role - Returns Code 401")
    @Test
    void testUpdateCity_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(put(CityController.CITY_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get City By ID")
    @Order(1)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetCityById_whenValidIdProvided_returnsCityDTO(String user, String password) throws Exception {
        mockMvc.perform(get(CityController.CITY_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Get City By ID Failed - Invalid ID Provided")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetCityById_whenInvalidIdProvided_returns4040StatusCode(String user, String password) throws Exception {
        mockMvc.perform(get(CityController.CITY_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get City By ID Failed - No Role - Returns Code 401")
    @Test
    void testGetCityById_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(CityController.CITY_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get All Cities")
    @Order(2)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetAllCities_whenListIsPopulated_returnsListOfCityDto(String user, String password) throws Exception {
        mockMvc.perform(get(CityController.CITY_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Get All Cities Failed - No Role - Returns Code 401")
    @Test
    void testGetAllCities_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(CityController.CITY_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Delete City")
    @Order(5)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeleteCity_whenValidIdProvided_returns204StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(CityController.CITY_URI_WITH_ID, 4)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete City Failed - Invalid ID provided")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeleteCity_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(CityController.CITY_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete City Failed - Moderator And User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_MODERATOR_AND_USER)
    void testDeleteCity_withModeratorAndUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(CityController.CITY_URI_WITH_ID, 2)
                .with(httpBasic(user, password))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Delete City Failed - No Role - Returns Code 401")
    @Test
    void testDeleteCity_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(delete(CityController.CITY_URI_WITH_ID,1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private CityDTO getTestCityDTO() {
        return CityDTO.builder()
                .name("New York")
                .zipCode("11111")
                .build();
    }
}