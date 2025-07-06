package com.myproject.autopartsestoresystem.parts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.util.BaseIT;
import com.myproject.autopartsestoresystem.parts.dto.PartGroupDTO;
import com.myproject.autopartsestoresystem.parts.entity.PartGroupType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

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
class PartGroupControllerIT extends BaseIT {

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
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreatePartGroup_whenValidDetailsProvided_returns201StatusCode(String user, String password) throws Exception {

        mockMvc.perform(post(PartGroupController.PARTGROUP_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partGroupDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", PartGroupController.PARTGROUP_URI + "/" + 4))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(partGroupDTO.getName().name()));

    }

    @DisplayName("Create Part Group Failed - Invalid Details Provided - Returns Code 400")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreatePartGroup_whenInvalidDetailsProvided_returns400StatusCode(String user, String password) throws Exception {

        partGroupDTO.setName(null);

        mockMvc.perform(post(PartGroupController.PARTGROUP_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partGroupDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Create Part Group Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testCreatePartGroup_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(post(PartGroupController.PARTGROUP_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partGroupDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreatePartGroup_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(post(PartGroupController.PARTGROUP_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Update Part Group")
    @Order(4)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testUpdatePartGroup_whenValidDetailsProvided_returns204StatusCode(String user, String password) throws Exception {

        partGroupDTO.setId(4L);
        partGroupDTO.setName(PartGroupType.ENGINE_COMPONENTS);

        mockMvc.perform(put(PartGroupController.PARTGROUP_URI_WITH_ID, partGroupDTO.getId())
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partGroupDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Update Part Group Failed - User Role - Returns Code 403")
    @Order(5)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testUpdatePartGroup_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(put(PartGroupController.PARTGROUP_URI_WITH_ID, 4)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partGroupDTO)))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Update Part Group Failed - No Role - Returns Code 401")
    @Test
    void testUpdatePartGroup_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(put(PartGroupController.PARTGROUP_URI_WITH_ID, partGroupDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(objectMapper.writeValueAsString(partGroupDTO)))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get Part Group By ID")
    @Order(1)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetPartGroupById_whenValidIdProvided_returnsPartGroupDTO(String user, String password) throws Exception {

        mockMvc.perform(get(PartGroupController.PARTGROUP_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @DisplayName("Get Part Group By ID Failed - Invalid ID Provided - Returns Code 404")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testGetPartGroupById_whenInvalidIdProvided_returns404StatusCode(String user,  String password) throws Exception {

        mockMvc.perform(get(PartGroupController.PARTGROUP_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get Part Group By ID Failed - No Role - Returns Code 401")
    @Test
    void testGetPartGroupById_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(PartGroupController.PARTGROUP_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("Get All Part Groups")
    @Order(2)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetAllPartGroups_whenListIsPopulated_returns200StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(PartGroupController.PARTGROUP_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @DisplayName("Get All Part Groups Failed - No Role - Returns Code 401")
    @Test
    void testGetAllPartGroups_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(PartGroupController.PARTGROUP_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Delete Part Group")
    @Order(6)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeletePartGroup_whenValidIdProvided_returns204StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(PartGroupController.PARTGROUP_URI_WITH_ID, 4)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Part Group Failed - Invalid ID Provided - Returns Code 404")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeletePartGroup_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {
        mockMvc.perform(delete(PartGroupController.PARTGROUP_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete Part Group Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testDeletePartGroup_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(PartGroupController.PARTGROUP_URI_WITH_ID, 1)
                .with(httpBasic(user, password))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Delete Part Group Failed - No Role - Returns Code 401")
    @Test
    void testDeletePartGroup_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(delete(PartGroupController.PARTGROUP_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}