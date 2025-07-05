package com.myproject.autopartsestoresystem.vehicles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.controller.BaseIT;
import com.myproject.autopartsestoresystem.vehicles.dto.VehicleDTO;
import com.myproject.autopartsestoresystem.brands.entity.Brand;
import com.myproject.autopartsestoresystem.models.entity.Model;
import com.myproject.autopartsestoresystem.models.entity.ModelId;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;

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
class VehicleControllerIT extends BaseIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private VehicleDTO vehicleDTO;

    @BeforeEach
    void setUp() {

        Model model = Model.builder()
                .id(new ModelId(1L, "318"))
                .brand(Brand.builder().id(1L).name("BMW").models(new HashSet<>()).build())
                .build();


        vehicleDTO = VehicleDTO.builder()
                .parts(new ArrayList<>())
                .model(model)
                .engineType("2.0i Injection")
                .series("Series 3")
                .build();
    }

    @DisplayName("Create Vehicle")
    @Order(3)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreateVehicle_whenValidDetailsProvided_returns201StatusCode(String user, String password) throws Exception {

        mockMvc.perform(post(VehicleController.VEHICLE_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", VehicleController.VEHICLE_URI + "/" + 2))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.engineType").value("2.0i Injection"));
    }

    @DisplayName("Create Vehicle Failed - Invalid Details Provided - Returns Code 400")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testCreateVehicle_whenInvalidDetailsProvided_returns400StatusCode(String user, String password) throws Exception {

        vehicleDTO.setModel(null);

        mockMvc.perform(post(VehicleController.VEHICLE_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isBadRequest());
    }


    @DisplayName("Create Vehicle Failed - User Role -Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testCreateVehicle_withUserRole_retuns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(post(VehicleController.VEHICLE_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Create Vehicle Failed - No Role - Returns Code 401")
    @Test
    void testCreateVehicle_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(post(VehicleController.VEHICLE_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Update Vehicle")
    @Order(4)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testUpdateVehicle_whenValidDetailsProvided_returns204StatusCode(String user, String password) throws Exception {

        vehicleDTO.setEngineType("2.2I injection");

        mockMvc.perform(put(VehicleController.VEHICLE_URI_WITH_ID, 2)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Update Vehicle Failed - Wrong ID Provided - Returns Code 400")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testUpdateVehicle_whenInvalidIdProvided_returns400StatusCode(String user, String password) throws Exception {

        mockMvc.perform(put(VehicleController.VEHICLE_URI_WITH_ID, 3)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Update Vehicle Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testUpdateVehicle_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(put(VehicleController.VEHICLE_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Update vehicle Failed - No Role - Returns Code 401")
    @Test
    void testUpdateVehicle_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(put(VehicleController.VEHICLE_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get All Vehicles")
    @Order(2)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetAllVehicles_whenListIsPopulated_returns200StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(VehicleController.VEHICLE_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

    }

    @DisplayName("Get All Vehicles Failed - No Role - Returns Code 401")
    @Test
    void testGetAllVehicles_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(VehicleController.VEHICLE_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get Vehicle By ID")
    @Order(1)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetVehicleById_whenValidIdProvided_returns200StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(VehicleController.VEHICLE_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.engineType").value("1.8i Injection"));
    }

    @DisplayName("Get Vehicle By ID - Invalid ID Provided - Returns Code 404")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ALL_USERS)
    void testGetVehicleById_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(VehicleController.VEHICLE_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get Vehicle By ID Failed - No Role - Returns Code 401")
    @Test
    void testGetVehicleByID_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(VehicleController.VEHICLE_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("Delete Vehicle")
    @Order(5)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeleteVehicle_whenValidIdProvided_returns204StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(VehicleController.VEHICLE_URI_WITH_ID, 2)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Vehicle - Invalid ID Provided - Returns Code 404")
    @ParameterizedTest
    @MethodSource(GET_ADMIN_USER)
    void testDeleteVehicle_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(VehicleController.VEHICLE_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete Vehicle Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testDeleteVehicle_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(VehicleController.VEHICLE_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Delete Vehicle Failed - No Role - Returns Code 401")
    @Test
    void testDeleteVehicle_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(delete(VehicleController.VEHICLE_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}