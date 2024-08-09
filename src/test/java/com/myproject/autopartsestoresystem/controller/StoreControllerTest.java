package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.StoreDTO;
import com.myproject.autopartsestoresystem.exception.service.StoreNotFoundException;
import com.myproject.autopartsestoresystem.model.City;
import com.myproject.autopartsestoresystem.model.Store;
import com.myproject.autopartsestoresystem.service.StoreService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @author Miroslav Kološnjaji
 */
@WebMvcTest(controllers = StoreController.class)
@MockBean(StoreService.class)
@AutoConfigureMockMvc(addFilters = false)
class StoreControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private StoreService storeService;
    
    private ObjectMapper objectMapper;
    private StoreDTO storeDTO;

    @BeforeEach
    void setUp() {

        objectMapper = new ObjectMapper();

        storeDTO = StoreDTO.builder()
                .name("Store XYZ")
                .email("xyz@example.com")
                .phoneNumber("123123123")
                .city(City.builder().build())
                .build();
    }

    @DisplayName("Create Store")
    @Test
    void testCreateStore_whenValidDetailsProvided_returns200StatusCode() throws Exception {

        //given
        when(storeService.save(storeDTO)).thenReturn(storeDTO);

        RequestBuilder requestBuilder = post(StoreController.STORE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        StoreDTO savedStoreDTO = objectMapper.readValue(response, StoreDTO.class);

        //then
        assertNotNull(savedStoreDTO, "Saved Store DTO cannot be null");
        assertEquals(storeDTO, savedStoreDTO, "Saved Store DTO is not the same");
        verify(storeService).save(storeDTO);

    }

    @DisplayName("Create Store Failed - Invalid Details Provided")
    @Test
    void testCreateStore_whenIvalidDetailsProvided_returns400StatusCode() throws Exception {

        //given
        storeDTO.setCity(null);

        RequestBuilder requestBuilder = post(StoreController.STORE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Update Store")
    @Test
    void testUpdateStore_whenValidDetailsProvided_returns204StatusCode() throws Exception {

        //given
        when(storeService.update(anyLong(), any(StoreDTO.class))).thenReturn(storeDTO);

        RequestBuilder requestBuilder = put(StoreController.STORE_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(storeService).update(anyLong(), any(StoreDTO.class));
    }

    @DisplayName("Update Store Failed - Invalid Details Provided")
    @Test
    void testUpdateStore_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        //given
        storeDTO.setCity(null);

        RequestBuilder requestBuilder = put(StoreController.STORE_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Get All Stores")
    @Test
    void testGetAllStores_whenListIsPopulated_returns200StatusCode() throws Exception {

        //given
        List<StoreDTO> storeDTOS = List.of(mock(StoreDTO.class), mock(StoreDTO.class));
        when(storeService.getAll()).thenReturn(storeDTOS);

        RequestBuilder requestBuilder = get(StoreController.STORE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDTOS));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        int size = objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size();

        //then
        assertEquals(2, size, "Expected 2 elements returned" );
    }

    @DisplayName("Get All Stores When List Is Empty")
    @Test
    void testGetAllStores_whenListIsEmpty_returns204StatusCode() throws Exception {

        //given
        List<StoreDTO> storeDTOS = List.of();
        when(storeService.getAll()).thenReturn(storeDTOS);

        RequestBuilder requestBuilder = get(StoreController.STORE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDTOS));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected" );
    }

    @DisplayName("Get Store By ID")
    @Test
    void testGetVehicleById_whenValidIdProvided_returns200StatusCode() throws Exception {

        //given
        when(storeService.getById(anyLong())).thenReturn(storeDTO);

        RequestBuilder requestBuilder = get(StoreController.STORE_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        StoreDTO foundDTO = objectMapper.readValue(response, StoreDTO.class);

        //then
        assertNotNull(foundDTO, "Found DTO cannot be null");
        assertEquals(storeDTO, foundDTO, "Found DTO is not the same");
        verify(storeService).getById(anyLong());
    }

    @DisplayName("Get Store By ID Failed - Invalid ID Provided")
    @Test
    void testGetStoreByID_whenInvalidIdProvided_returns400StatusCode() throws Exception {

        //given
        doThrow(StoreNotFoundException.class).when(storeService).getById(anyLong());

        RequestBuilder requestBuilder = get(StoreController.STORE_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        verify(storeService).getById(anyLong());
    }

    @DisplayName("Delete Store")
    @Test
    void testDeleteStoreByID_whenValidIDProvided_returns204StatusCode() throws Exception {

        //given
        doNothing().when(storeService).delete(anyLong());

        RequestBuilder requestBuilder = delete(StoreController.STORE_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");

    }

    @Test
    void testDeleteStoreByID_whenInvalidIDProvided_returns404StatusCode() throws Exception {

        //given
        doThrow(StoreNotFoundException.class).when(storeService).delete(anyLong());

        RequestBuilder requestBuilder = delete(StoreController.STORE_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
    }
}