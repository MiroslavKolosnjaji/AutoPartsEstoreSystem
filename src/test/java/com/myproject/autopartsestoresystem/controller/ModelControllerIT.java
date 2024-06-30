package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.ModelDTO;
import com.myproject.autopartsestoresystem.model.Brand;
import com.myproject.autopartsestoresystem.model.ModelId;
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
class ModelControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("Create Model")
    @Order(3)
    @Test
    void testCreateModel_whenValidDetailsProvided_returns201StatusCode() throws Exception {

        ModelDTO modelDTO = getTestModelDTO();

        mockMvc.perform(post(ModelController.MODEL_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modelDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", ModelController.MODEL_URI + "/" + modelDTO.getId().getId() + "/" + modelDTO.getId().getName()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id.name").value(modelDTO.getId().getName()));

    }

    @DisplayName("Create Model Failed - Invalid Details Provided - Returns Code 400")
    @Order(99)
    @Test
    void testCreateModel_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {
        ModelDTO modelDTO = getTestModelDTO();
        modelDTO.setBrand(null);

        mockMvc.perform(post(ModelController.MODEL_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modelDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Update Model")
    @Order(4)
    @Test
    void testUpdateModel_whenValidDetailsProvided_returns204StatusCode() throws Exception {

        ModelDTO modelDTO = getTestModelDTO();
        modelDTO.setId(new ModelId(1L, "316"));
        modelDTO.getId().setName("318");

        mockMvc.perform(put(ModelController.MODEL_URI_WITH_ID, 1L, modelDTO.getId().getName())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modelDTO)))
                .andExpect(status().isNoContent());

    }

    @DisplayName("Get Model By ID")
    @Order(1)
    @Test
    void testGetModelById_whenValidIdProvided_returnsModelDTO() throws Exception {

        mockMvc.perform(get(ModelController.MODEL_URI_WITH_ID, 1,"316")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.id.name").value("316"));
    }

    @DisplayName("Get Model By ID Failed - Invalid ID Provided - Returns Code 404")
    @Test
    void testGetModelById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        mockMvc.perform(get(ModelController.MODEL_URI_WITH_ID, "325",99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get All Models")
    @Order(2)
    @Test
    void testGetAllModels_whenListIsPopulated_returnsListOfModelDtos() throws Exception {

        mockMvc.perform(get(ModelController.MODEL_URI)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Delete Model")
    @Order(5)
    @Test
    void testDeleteModel_whenValidIdProvided_returns204StatusCode() throws Exception {

        mockMvc.perform(delete(ModelController.MODEL_URI_WITH_ID, 1, "330")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Model Failed - Invalid ID Provided - Returns 404 Status Code")
    @Test
    void testDeleteModel_whenInvalidIdProvided_returns404StatusCode() throws Exception {
        mockMvc.perform(delete(ModelController.MODEL_URI_WITH_ID, 99, "325")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private ModelDTO getTestModelDTO() {
        return ModelDTO.builder().id(new ModelId(1L, "330")).brand(Brand.builder().id(1L).name("BMW").build()).build();
    }
}