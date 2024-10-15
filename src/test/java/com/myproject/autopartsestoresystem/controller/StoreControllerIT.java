package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.CityDTO;
import com.myproject.autopartsestoresystem.dto.StoreDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.mapper.CityMapper;
import com.myproject.autopartsestoresystem.model.Store;
import com.myproject.autopartsestoresystem.service.CityService;
import org.hibernate.annotations.Parameter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
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
class StoreControllerIT extends BaseIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CityService cityService;

    @Autowired
    private CityMapper cityMapper;

    @Autowired
    private MockMvc mockMvc;

    private StoreDTO storeDTO;

    @BeforeEach
    void setUp() throws EntityNotFoundException {

        CityDTO cityDTO = cityService.getById(1L);

        storeDTO = StoreDTO.builder()
                .name("Store XYZ")
                .email("xyz@example.com")
                .phoneNumber("123123123")
                .city(cityMapper.cityDTOToCity(cityDTO))
                .build();

    }

    @DisplayName("Create Store")
    @Order(1)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreateStore_whenValidDetailsProvided_returns201StatusCode(String user, String password) throws Exception {

        mockMvc.perform(post(StoreController.STORE_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", StoreController.STORE_URI + "/" + 2))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Store XYZ"))
                .andExpect(jsonPath("$.email").value("xyz@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("123123123"));
    }

    @DisplayName("Create Store Failed - Invalid Details Provided")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreateStore_whenInvalidDetailsProvided_returns400StatusCode(String user, String password) throws Exception {

        storeDTO.setCity(null);

        mockMvc.perform(post(StoreController.STORE_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Create Store Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testCreateStore_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(post(StoreController.STORE_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeDTO)))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Create Store Failed - No Role - Returns Code 401")
    @Test
    void testCreateStore_withoutAnyUser_returns401StatusCode() throws Exception {

        mockMvc.perform(post(StoreController.STORE_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Update Store")
    @Order(2)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testUpdateStore_whenValidDetailsProvided_returns204StatusCode(String user, String password) throws Exception {

        storeDTO.setName("Store Y");

        mockMvc.perform(put(StoreController.STORE_URI_WITH_ID, 2)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Update Store Failed - Invalid ID Provided")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testUpdateStore_whenInvalidIdProvided_returns400StatusCode(String user, String password) throws Exception {

        mockMvc.perform(put(StoreController.STORE_URI_WITH_ID, 3)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Update Store Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testUpdateStore_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(put(StoreController.STORE_URI_WITH_ID, 3)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeDTO)))
                .andExpect(status().isForbidden());
    }


    @DisplayName("Update Store Failed - No Role - Returns Code 401")
    @Test
    void testUpdateStore_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(put(StoreController.STORE_URI_WITH_ID, 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get All Stores")
    @Order(3)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testGetAllStores_whenListIsPopulated_returns200StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(StoreController.STORE_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @DisplayName("Get All Stores Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testGetAllStores_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(StoreController.STORE_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Get All Stores Failed - No Role - Returns Code 401")
    @Test
    void testGetAllStores_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(StoreController.STORE_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get Store By ID")
    @Order(4)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testGetStoreById_whenValidIdProvided_returns200StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(StoreController.STORE_URI_WITH_ID, 2)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.id").value(2));
    }

    @DisplayName("Get Store By ID Failed - Invalid ID Provided")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testGetStoreByID_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(StoreController.STORE_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get Store By ID Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testGetStoreByID_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(StoreController.STORE_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Get Store By ID Failed - No Role - Returns Code 401")
    @Test
    void testGetStoreByID_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(StoreController.STORE_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @DisplayName("Delete Store")
    @Order(5)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeleteStore_whenValidIdProvided_returns204StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(StoreController.STORE_URI_WITH_ID, 2)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Store Failed - Invalid ID Provided")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeleteStore_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(StoreController.STORE_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete Store Failed - Moderator And User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_MODERATOR_AND_USER)
    void testDeleteStore_withModeratorAndUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(StoreController.STORE_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Delete Store Failed - No Role - Returns Code 401")
    @Test
    void testDeleteStore_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(delete(StoreController.STORE_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}