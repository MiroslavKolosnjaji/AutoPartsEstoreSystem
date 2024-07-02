package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.ModelDTO;
import com.myproject.autopartsestoresystem.exception.service.ModelNotFoundException;
import com.myproject.autopartsestoresystem.model.Brand;
import com.myproject.autopartsestoresystem.model.ModelId;
import com.myproject.autopartsestoresystem.service.ModelService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@WebMvcTest(value = ModelController.class)
@MockBean(ModelService.class)
class ModelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelService modelService;

    private ObjectMapper objectMapper;

    private ModelDTO modelDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        modelDTO = ModelDTO.builder().id(new ModelId(1L, "330")).brand(new Brand(1L, "BMW", null)).build();
    }


    @DisplayName("Create Model")
    @Test
    void testCreateModel_whenValidDetailsProvided_returnsCreatedModelDTO() throws Exception {

        //given
        when(modelService.save(any(ModelDTO.class))).thenReturn(modelDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(ModelController.MODEL_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modelDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ModelDTO savedDTO = objectMapper.readValue(response, ModelDTO.class);

        assertAll("Saved Model Validation",
                () -> assertEquals(modelDTO.getId().getId(), savedDTO.getId().getId(), "Id doesn't match"),
                () -> assertEquals(modelDTO.getId().getName(), savedDTO.getId().getName(), "Name doesn't match"),
                () -> assertEquals(savedDTO.getBrand(), modelDTO.getBrand(), "Brand doesn't match"));

        verify(modelService).save(any(ModelDTO.class));
    }

    @DisplayName("Create Model When Invalid Details Provided - Returns Status 400 ")
    @Test
    void testCreateModel_whenModelNameIsNotProvided_returns400StatusCode() throws Exception {

        //given
        modelDTO.setId(new ModelId(1L, ""));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(ModelController.MODEL_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modelDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Update Model")
    @Test
    void testUpdateModel_whenValidModelDetailsProvided_Returns204StatusCode() throws Exception {

        //given
        modelDTO.setId(new ModelId(2L, "A7"));
        modelDTO.setBrand(new Brand(2L, "AUDI", null));

        when(modelService.update(any(ModelId.class), any(ModelDTO.class))).thenReturn(modelDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(ModelController.MODEL_URI_WITH_ID, modelDTO.getId().getId(), modelDTO.getId().getName())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modelDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(modelService).update(any(ModelId.class), any(ModelDTO.class));
    }

    @DisplayName("Update Model When Invalid Model Details Provided - Returns Status 400")
    @Test
    void testUpdateModel_whenInvalidModelDetailsProvided_returns400StatusCode() throws Exception {

        //given
        modelDTO.setId(new ModelId(1L, "330"));
        modelDTO.setBrand(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(ModelController.MODEL_URI_WITH_ID, modelDTO.getId().getId(), modelDTO.getId().getName())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modelDTO));


        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Get All Models")
    @Test
    void testGetAllModels_whenListIsPopulated_returnsTwoModels() throws Exception {

        //given
        List<ModelDTO> models = getModels();
        when(modelService.getAll()).thenReturn(models);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(ModelController.MODEL_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(models));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(2, objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size(), "Expected 2 models to be returned");
        verify(modelService).getAll();
    }

    @DisplayName("Get Model By ID")
    @Test
    void testGetModel_whenValidIdProvided_returnsModelDTOWithID1() throws Exception {

        //given
        modelDTO.setId(new ModelId(1L, "330"));
        when(modelService.getById(any(ModelId.class))).thenReturn(modelDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(ModelController.MODEL_URI_WITH_ID, modelDTO.getId().getId(), modelDTO.getId().getName())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modelDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        ModelDTO foundDTO = objectMapper.readValue(response, ModelDTO.class);

        //then
        assertAll("Get Model By ID Validation",
                () -> assertEquals(modelDTO.getId().getId(), foundDTO.getId().getId(), "Id doesn't match"),
                () -> assertEquals(modelDTO.getId().getName(), foundDTO.getId().getName(), "Name doesn't match"),
                () -> assertEquals(modelDTO.getBrand(), foundDTO.getBrand(), "Brand doesn't match"));

        verify(modelService).getById(any(ModelId.class));

    }

    @DisplayName("Get Model By ID When Invalid ID Provided - Returns 404 Status Code")
    @Test
    void testGetModel_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        modelDTO.setId(new ModelId(15L, "XC"));
        when(modelService.getById(any(ModelId.class))).thenThrow(ModelNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(ModelController.MODEL_URI_WITH_ID, modelDTO.getId().getId(), modelDTO.getId().getName())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        verify(modelService).getById(any(ModelId.class));
    }

    @DisplayName("Delete Model")
    @Test
    void testDeleteModel_whenValidIDProvided_returns204StatusCode() throws Exception {

        //given
        modelDTO.setId(new ModelId(1L, "330"));
        doNothing().when(modelService).delete(modelDTO.getId());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(ModelController.MODEL_URI_WITH_ID, modelDTO.getId().getId(), modelDTO.getId().getName())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(modelService).delete(modelDTO.getId());
    }

    @DisplayName("Delete Model When Ivalid ID Provided - Returns 404 Status Code")
    @Test
    void testDeleteCity_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        final String MESSAGE = "Model not found";
        modelDTO.setId(new ModelId(15L, "330"));
        doThrow(new ModelNotFoundException(MESSAGE)).when(modelService).delete(modelDTO.getId());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(ModelController.MODEL_URI_WITH_ID, modelDTO.getId().getId(), modelDTO.getId().getName())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();


        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        assertTrue(response.contains(MESSAGE), "Error message doesn't match");
        verify(modelService).delete(modelDTO.getId());
    }

    private List<ModelDTO> getModels(){
        return List.of(modelDTO, ModelDTO.builder().id(new ModelId(2L,"A8")).brand(new Brand(2L, "AUDI", null)).build());
    }
}