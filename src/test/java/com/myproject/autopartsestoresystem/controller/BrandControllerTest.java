package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.BrandDTO;
import com.myproject.autopartsestoresystem.exception.service.BrandNotFoundException;
import com.myproject.autopartsestoresystem.service.BrandService;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@WebMvcTest(value = BrandController.class)
@MockBean({BrandService.class})
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BrandService brandService;

    ObjectMapper objectMapper;

    BrandDTO brandDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        brandDTO = BrandDTO.builder().name("AUDI").build();
    }

    @DisplayName("Create Brand")
    @Test
    void testCreateBrand_whenValidBrandDetailsProvided_returnsCreatedBrandDTO() throws Exception {

        //given
        when(brandService.save(any(BrandDTO.class))).thenReturn(brandDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BrandController.BRAND_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        BrandDTO savedBrandDTO = objectMapper.readValue(response, BrandDTO.class);

        assertEquals(brandDTO.getName(), savedBrandDTO.getName(), "Brand name doesn't match");

        verify(brandService).save(any(BrandDTO.class));
    }

    @DisplayName("Create Brand With Invalid Brand Details Provided - Returns Status 400")
    @Test
    void testCreateBrand_whenBrandNameIsNotProvided_returns400StatusCode() throws Exception {

        //given
        brandDTO.setName("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(BrandController.BRAND_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Update Brand")
    @Test
    void testUpdateBrand_whenValidBrandDetailsProvided_returns204StatusCode() throws Exception {

        //given
        brandDTO.setId(1L);
        brandDTO.setName("Rolls-Royce");
        when(brandService.update(any(Long.class), any(BrandDTO.class))).thenReturn(brandDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(BrandController.BRAND_URI_WITH_ID, brandDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(brandService).update(any(Long.class), any(BrandDTO.class));
    }

    @DisplayName("Update Brand With Invalid Brand Details Provided - Returns Status 400")
    @Test
    void testUpdateBrand_whenInvalidBrandDetailsProvided_returns400StatusCode() throws Exception {

        //given
        brandDTO.setId(1L);
        brandDTO.setName("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(BrandController.BRAND_URI_WITH_ID, brandDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Get All Brands")
    @Test
    void testGetAllBrands_whenListIsPopulated_returnsTwoBrands() throws Exception {

        //given
        List<BrandDTO> brands = getBrands();
        when(brandService.getAll()).thenReturn(brands);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BrandController.BRAND_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brands));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(2, objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size(), "Expected 2 brands to be returned");
    }

    @DisplayName("Get Brand")
    @Test
    void testGetBrand_whenValidIdProvided_returnsBrandDtoWithId1() throws Exception {

        //given
        brandDTO.setId(1L);
        when(brandService.getById(anyLong())).thenReturn(brandDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BrandController.BRAND_URI_WITH_ID, brandDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        BrandDTO getBrandDTO = objectMapper.readValue(response, BrandDTO.class);

        assertAll("Get Brand by ID validation",
                () -> assertEquals(brandDTO.getId(), getBrandDTO.getId(), "Brand id doesn't match"),
                () -> assertEquals(brandDTO.getName(), getBrandDTO.getName(), "Brand name doesn't match"));

        verify(brandService).getById(anyLong());
    }

    @DisplayName("Get Brand When Invalid ID Provided - Returns 404 Status Code ")
    @Test
    void testGetBrand_whenInvalidIdProvided_returns404StatusCode() throws Exception{

        //given
        Long id = 100L;
        when(brandService.getById(anyLong())).thenThrow(BrandNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(BrandController.BRAND_URI_WITH_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        verify(brandService).getById(anyLong());
    }

    @Test
    void testDeleteBrand_whenValidIdProvided_returns204StatusCode() throws Exception {

        //given
        Long id = 1L;
        doNothing().when(brandService).delete(id);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(BrandController.BRAND_URI_WITH_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(brandService).delete(id);
    }

    @Test
    void testDeleteBrand_whenInvalidIdProvided_throwsBrandDoesntExistsExceptionAndReturns404StatusCode() throws Exception {

        //given
        Long invalidId = 100L;
        doThrow(BrandNotFoundException.class).when(brandService).delete(invalidId);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(BrandController.BRAND_URI_WITH_ID, invalidId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String responseBody = result.getResponse().getContentAsString();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        verify(brandService).delete(invalidId);
    }

    private List<BrandDTO> getBrands() {
        return Arrays.asList(brandDTO, BrandDTO.builder().id(2L).name("BMW").build() );
    }
}