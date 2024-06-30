package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.PartGroupDTO;
import com.myproject.autopartsestoresystem.model.PartGroupType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

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
class PartGroupControllerIT {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    private PartGroupDTO partGroupDTO;

    @BeforeEach
    void setUp() {
        partGroupDTO = PartGroupDTO.builder()
                .name(PartGroupType.FUEL_SYSTEM)
                .parts(new ArrayList<>())
                .build();
    }

    @DisplayName("Create Part Group")
    @Order(3)
    @Test
    void testCreatePartGroup_whenValidDetailsProvided_returns201StatusCode() throws Exception {

        mockMvc.perform(post(PartGroupController.PARTGROUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partGroupDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", PartGroupController.PARTGROUP_URI + "/" + 4))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(partGroupDTO.getName().name()));

    }

    @DisplayName("Create Part Group Failed - Invalid Details Provided - Returns Code 400")
    @Test
    void testCreatePartGroup_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        partGroupDTO.setName(null);

        mockMvc.perform(post(PartGroupController.PARTGROUP_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partGroupDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Update Part Group")
    @Order(4)
    @Test
    void testUpdatePartGroup_whenValidDetailsProvided_returns204StatusCode() throws Exception {

        partGroupDTO.setId(4L);
        partGroupDTO.setName(PartGroupType.ENGINE_COMPONENTS);

        mockMvc.perform(put(PartGroupController.PARTGROUP_URI_WITH_ID, partGroupDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partGroupDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Get Part Group By ID")
    @Order(1)
    @Test
    void testGetPartGroupById_whenValidIdProvided_returnsPartGroupDTO()throws Exception {


        mockMvc.perform(get(PartGroupController.PARTGROUP_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Get Part Group By ID Failed - Invalid ID Provided - Returns Code 404")
    @Test
    void testGetPartGroupById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        mockMvc.perform(get(PartGroupController.PARTGROUP_URI_WITH_ID, 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get All Part Groups")
    @Order(2)
    @Test
    void testGetAllPartGroups_whenListIsPopulated_returnsListOfModelDtos() throws Exception {

        mockMvc.perform(get(PartGroupController.PARTGROUP_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Delete Part Group")
    @Order(5)
    @Test
    void testDeletePartGroup_whenValidIdProvided_returns204StatusCode() throws Exception {

        mockMvc.perform(delete(PartGroupController.PARTGROUP_URI_WITH_ID, 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Part Group Failed - Invalid ID Provided - Returns 404 Status Code")
    @Test
    void testDeletePartGroup_whenInvalidIdProvided_returns404StatusCode() throws Exception {
        mockMvc.perform(delete(PartGroupController.PARTGROUP_URI_WITH_ID, 99)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}