package com.myproject.autopartsestoresystem.models.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.controller.BaseIT;
import com.myproject.autopartsestoresystem.models.dto.ModelDTO;
import com.myproject.autopartsestoresystem.brands.entity.Brand;
import com.myproject.autopartsestoresystem.models.entity.ModelId;
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
class ModelControllerIT extends BaseIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("Create Model")
    @Order(1)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreateModel_whenValidDetailsProvided_returns201StatusCode(String user, String password) throws Exception {

        ModelDTO modelDTO = getTestModelDTO();

        mockMvc.perform(post(ModelController.MODEL_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", ModelController.MODEL_URI + "/" + 1 + "/" + "330"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id.name").value(modelDTO.getId().getName()));

    }

    @DisplayName("Create Model Failed - Invalid Details Provided - Returns Code 400")
    @Order(99)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreateModel_whenInvalidDetailsProvided_returns400StatusCode(String user, String password) throws Exception {
        ModelDTO modelDTO = getTestModelDTO();
        modelDTO.setBrand(null);

        mockMvc.perform(post(ModelController.MODEL_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Create Model Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testCreateModel_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        ModelDTO modelDTO = getTestModelDTO();
                modelDTO.getId().setName("350");

        mockMvc.perform(post(ModelController.MODEL_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelDTO)))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Create Model Failed - No Role - Returns Code 401")
    @Test
    void testCreateModel_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(post(ModelController.MODEL_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Update Model")
    @Order(2)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testUpdateModel_whenValidDetailsProvided_returns204StatusCode(String user, String password) throws Exception {

        ModelDTO modelDTO = getTestModelDTO();
        modelDTO.setId(new ModelId(1L, "316"));
        modelDTO.getBrand().setName("AUDI");

        mockMvc.perform(put(ModelController.MODEL_URI_WITH_ID, 1L, modelDTO.getId().getName())
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelDTO)))
                .andExpect(status().isNoContent());

    }

    @DisplayName("Update Model Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testUpdateModel_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        ModelDTO modelDTO = getTestModelDTO();

        mockMvc.perform(put(ModelController.MODEL_URI_WITH_ID, 1L, modelDTO.getId().getName())
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modelDTO)))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Get Model By ID")
    @Order(3)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetModelById_whenValidIdProvided_returns200StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(ModelController.MODEL_URI_WITH_ID, 1, "316")
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.id.name").value("316"));
    }

    @DisplayName("Get Model By ID Failed - Invalid ID Provided - Returns Code 404")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetModelById_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(ModelController.MODEL_URI_WITH_ID, "325", 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get Model By ID Failed - No Role - Returns Code 401")
    @Test
    void testGetModelById_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(ModelController.MODEL_URI_WITH_ID, 1, "316")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get All Models")
    @Order(5)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetAllModels_whenListIsPopulated_returnsListOfModelDtos(String user, String password) throws Exception {

        mockMvc.perform(get(ModelController.MODEL_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(4));
    }

    @DisplayName("Get All Models Failed - No Role - Returns Code 401")
    @Test
    void testGetAllModels_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(ModelController.MODEL_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Delete Model")
    @Order(4)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeleteModel_whenValidIdProvided_returns204StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(ModelController.MODEL_URI_WITH_ID, 1, "330")
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Model Failed - Invalid ID Provided - Returns Code 404")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeleteModel_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {
        mockMvc.perform(delete(ModelController.MODEL_URI_WITH_ID, 99, "325")
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete Model Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_MODERATOR_AND_USER)
    void testDeleteModel_withModeratorAndUserRole_returns403StatusCode(String user, String password) throws Exception {
        mockMvc.perform(delete(ModelController.MODEL_URI_WITH_ID, 1, "330")
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Delete Model Failed - No Role - Returns Code 401")
    @Test
    void testDeleteModel_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(delete(ModelController.MODEL_URI_WITH_ID, 1, "330")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private ModelDTO getTestModelDTO() {
        return ModelDTO.builder().id(new ModelId(1L, "330")).brand(Brand.builder().id(1L).name("BMW").build()).build();
    }
}