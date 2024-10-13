package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.CityDTO;
import com.myproject.autopartsestoresystem.dto.StoreDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.mapper.CityMapper;
import com.myproject.autopartsestoresystem.service.CityService;
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
@Disabled("Class is disabled pending security updates")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StoreControllerIT {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CityService cityService;

    @Autowired
    private CityMapper cityMapper;


    private StoreDTO storeDTO;
    @Autowired
    private MockMvc mockMvc;

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
    @Test
    void testCreateStore_whenValidDetailsProvided_returns201StatusCode() throws Exception {

        mockMvc.perform(post(StoreController.STORE_URI)
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
    @Test
    void testCreateStore_whenIvalidDetailsProvided_returns400StatusCode() throws Exception {

        storeDTO.setCity(null);

        mockMvc.perform(post(StoreController.STORE_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Update Store")
    @Order(2)
    @Test
    void testUpdateStore_whenValidDetailsProvided_returns204StatusCode() throws Exception {

        storeDTO.setName("Store Y");

        mockMvc.perform(put(StoreController.STORE_URI_WITH_ID, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(storeDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Update Store Failed - Invalid ID Provided")
    @Test
    void testUpdateStore_whenInvalidIdProvided_returns400StatusCode() throws Exception {

        mockMvc.perform(put(StoreController.STORE_URI_WITH_ID, 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Get All Stores")
    @Order(3)
    @Test
    void testGetAllStores_whenListIsPopulated_returns200StatusCode() throws Exception {

        mockMvc.perform(get(StoreController.STORE_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @DisplayName("Get Store By ID")
    @Order(4)
    @Test
    void testGetStoreById_whenValidIdProvided_returns200StatusCode() throws Exception {

        mockMvc.perform(get(StoreController.STORE_URI_WITH_ID, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-Type", "application/json"))
                .andExpect(jsonPath("$.id").value(2));
    }

    @DisplayName("Get Store By ID Failed - Invalid ID Provided")
    @Test
    void testGetStoreByID_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        mockMvc.perform(get(StoreController.STORE_URI_WITH_ID, 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete Store")
    @Order(5)
    @Test
    void testDeleteStore_whenValidIdProvided_returns204StatusCode() throws Exception {

        mockMvc.perform(delete(StoreController.STORE_URI_WITH_ID, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Store Failed - Invalid ID Provided")
    @Test
    void testDeleteStore_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        mockMvc.perform(delete(StoreController.STORE_URI_WITH_ID, 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}