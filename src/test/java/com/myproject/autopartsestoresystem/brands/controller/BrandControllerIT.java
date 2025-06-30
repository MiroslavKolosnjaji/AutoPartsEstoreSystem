package com.myproject.autopartsestoresystem.brands.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.brands.dto.BrandDTO;
import com.myproject.autopartsestoresystem.controller.BaseIT;
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
class BrandControllerIT extends BaseIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @DisplayName("Create Brand")
    @Order(3)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreateBrand_whenValidDetailsProvided_returns201StatusCode(String user, String password) throws Exception {

        BrandDTO brandDTO = getBrandDTO();

        mockMvc.perform(post(BrandController.BRAND_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", BrandController.BRAND_URI + "/4"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(brandDTO.getName()));
    }

    @DisplayName("Create Brand Failed - Invalid Details Provided - Returns Code 400")
    @Order(99)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreateBrand_whenInvalidDetailsProvided_returns400StatusCode(String user, String password) throws Exception {

        BrandDTO brandDTO = getBrandDTO();
        brandDTO.setName("");

        mockMvc.perform(post(BrandController.BRAND_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDTO)))
                .andExpect(status().isBadRequest());

    }

    @DisplayName("Create Brand Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testCreateBrand_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        BrandDTO brandDTO = getBrandDTO();

        mockMvc.perform(post(BrandController.BRAND_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDTO)))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Create Brand Failed - No Role - Returns Code 401")
    @Test
    void testCreateBrand_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(post(BrandController.BRAND_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Update Brand")
    @Order(4)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testUpdateBrand_whenValidDetailsProvided_returns204StatusCode(String user, String password) throws Exception {
        BrandDTO brandDTO = getBrandDTO();
        brandDTO.setId(4L);
        brandDTO.setName("test");

        mockMvc.perform(put(BrandController.BRAND_URI_WITH_ID, brandDTO.getId())
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Update Brand Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testUpdateBrand_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        BrandDTO brandDTO = getBrandDTO();
        brandDTO.setId(4L);
        brandDTO.setName("TEST");

        mockMvc.perform(put(BrandController.BRAND_URI_WITH_ID, brandDTO.getId())
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brandDTO)))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Update Brand Failed - No Role - Returns Code 401")
    @Test
    void testUpdateBrand_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(put(BrandController.BRAND_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get Brand By ID")
    @Order(1)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetBrandById_whenValidIdProvided_returnsBrandDTO(String user, String password) throws Exception {

        mockMvc.perform(get(BrandController.BRAND_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.id").value(1));
    }


    @DisplayName("Get Brand By ID Failed - Invalid ID Provided - Returns Code 404")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetBrandById_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {
        mockMvc.perform(get(BrandController.BRAND_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get Brand By ID Failed - No Role - Returns Code 401")
    @Test
    void testGetRoleById_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(BrandController.BRAND_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get All Brands")
    @Order(2)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetAllBrands_whenListIsPopulated_returnsListOfBrandDTO(String user, String password) throws Exception {
        mockMvc.perform(get(BrandController.BRAND_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Get All Brands - No Role - Returns Code 401")
    @Test
    void testGetAllBrands_withoutAnyRole_returnsStatus401StatusCode() throws Exception {

        mockMvc.perform(get(BrandController.BRAND_URI)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Delete City")
    @Order(5)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeleteBrand_whenValidIdProvided_returns204StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(BrandController.BRAND_URI_WITH_ID, 4)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Brand Failed - Invalid ID Provided 0 Returns 404 Status Code")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeleteBrand_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {
        mockMvc.perform(delete(BrandController.BRAND_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete Brand Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testDeleteBrand_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(BrandController.BRAND_URI_WITH_ID, 1)
                .with(httpBasic(user, password))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Delete Brand Failed - No Role - Returns Code 401")
    @Test
    void testDeleteBrand_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(delete(BrandController.BRAND_URI_WITH_ID,1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private BrandDTO getBrandDTO() {
        return BrandDTO.builder().name("Aston Martin").build();
    }

}