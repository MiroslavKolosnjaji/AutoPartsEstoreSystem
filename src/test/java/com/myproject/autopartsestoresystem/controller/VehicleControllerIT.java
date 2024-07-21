package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.VehicleDTO;
import com.myproject.autopartsestoresystem.model.Brand;
import com.myproject.autopartsestoresystem.model.Model;
import com.myproject.autopartsestoresystem.model.ModelId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehicleControllerIT {

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
    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void testCreateVehicle_whenValidDetailsProvided_returns201StatusCode() throws Exception {

        mockMvc.perform(post(VehicleController.VEHICLE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", VehicleController.VEHICLE_URI + "/" + 2))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.engineType").value("2.0i Injection"));
    }

    @DisplayName("Create Vehicle Failed - Invalid Details Provided - Returns Code 400")
    @Test
    void testCreateVehicle_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        vehicleDTO.setModel(null);

        mockMvc.perform(post(VehicleController.VEHICLE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Update Vehicle")
    @Order(4)
    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void testUpdateVehicle_whenValidDetailsProvided_returns204StatusCode() throws Exception {

        vehicleDTO.setEngineType("2.2I injection");

        mockMvc.perform(put(VehicleController.VEHICLE_URI_WITH_ID, 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Update Vehicle Failed - Wrong ID Provided - Returns Code 400")
    @Test
    void testUpdateVehicle_whenInvalidIdProvided_returns400StatusCode() throws Exception {

        mockMvc.perform(put(VehicleController.VEHICLE_URI_WITH_ID, 3)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Get All Vehicles")
    @Order(2)
    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void testGetAllVehicles_whenListIsPopulated_returns200StatusCode() throws Exception {

        mockMvc.perform(get(VehicleController.VEHICLE_URI)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));

    }

    @DisplayName("Get Vehicle By ID")
    @Order(1)
    @Test
    void testGetVehicleById_whenValidIdProvided_returns200StatusCode() throws Exception {

        mockMvc.perform(get(VehicleController.VEHICLE_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.engineType").value("1.8i Injection"));
    }

    @DisplayName("Get Vehicle By ID - Invalid ID Provided - Returns Code 404")
    @Test
    void testGetVehicleById_whenInvalidIdProvided_returns404StatusCode()throws Exception {

        mockMvc.perform(get(VehicleController.VEHICLE_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete Vehicle")
    @Order(5)
    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void testDeleteVehicle_whenValidIdProvided_returns204StatusCode() throws Exception {

        mockMvc.perform(delete(VehicleController.VEHICLE_URI_WITH_ID, 2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete Vehicle - Invalid ID Provided - Returns Code 404")
    @Test
    @WithMockUser(username = "admin@example.com", roles = "ADMIN")
    void testDeleteVehicle_whenIvalidIdProvided_returns404StatusCode() throws Exception {

        mockMvc.perform(delete(VehicleController.VEHICLE_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}