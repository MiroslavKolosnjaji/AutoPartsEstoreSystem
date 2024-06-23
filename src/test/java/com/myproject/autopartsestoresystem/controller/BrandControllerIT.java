package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.customer.BrandDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BrandControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("Create Brand")
    @Order(3)
    @Test
    void testCreateBrand_whenValidDetailsProvided_returns201StatusCode() throws Exception {

        BrandDTO brandDTO = getBrandDTO();

        mockMvc.perform(post(BrandController.BRAND_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", BrandController.BRAND_URI + "/4"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(brandDTO.getName()));
    }

    @DisplayName("Create Brand Failed - Invalid Details Provided - Returns Code 400")
    @Order(99)
    @Test
    void testCreateBrand_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        BrandDTO brandDTO = getBrandDTO();
        brandDTO.setName("");

        mockMvc.perform(post(BrandController.BRAND_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandDTO)))
                .andExpect(status().isBadRequest());

    }

    @DisplayName("Update Brand")
    @Order(4)
    @Test
    void testUpdateBrand_whenValidDetailsProvided_returns204StatusCode() throws Exception {
        BrandDTO brandDTO = getBrandDTO();
        brandDTO.setId(4L);
        brandDTO.setName("test");

        mockMvc.perform(put(BrandController.BRAND_URI_WITH_ID, brandDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Get Brand By ID")
    @Order(1)
    @Test
    void testGetBrandById_whenValidIdProvided_returnsBrandDTO() throws Exception {

        mockMvc.perform(get(BrandController.BRAND_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.id").value(1));
    }


    @DisplayName("Get Brand By ID Failed - Invalid ID Provided - Returns Code 404")
    @Test
    void testGetBrandById_whenInvalidIdProvided_returns404StatusCode() throws Exception {
        mockMvc.perform(get(BrandController.BRAND_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get All Brands")
    @Order(2)
    @Test
    void testGetAllBrands_whenListIsPopulated_returnsListOfBrandDTO() throws Exception {
        mockMvc.perform(get(BrandController.BRAND_URI)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Delete City")
    @Order(5)
    @Test
    void testDeleteBrand_whenValidIdProvided_returns204StatusCode() throws Exception {

        mockMvc.perform(delete(BrandController.BRAND_URI_WITH_ID, 4)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Brand Failed - Invalid ID Provided 0 Returns 404 Status Code")
    @Test
    void testDeleteBrand_whenInvalidIdProvided_returns404StatusCode() throws Exception {
        mockMvc.perform(delete(BrandController.BRAND_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private BrandDTO getBrandDTO(){
        return BrandDTO.builder().name("Aston Martin").build();
    }

}