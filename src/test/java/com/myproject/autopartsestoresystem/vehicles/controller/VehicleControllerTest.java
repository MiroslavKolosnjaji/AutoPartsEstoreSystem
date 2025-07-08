package com.myproject.autopartsestoresystem.vehicles.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.vehicles.dto.VehicleDTO;
import com.myproject.autopartsestoresystem.vehicles.exception.VehicleNotFoundException;
import com.myproject.autopartsestoresystem.brands.entity.Brand;
import com.myproject.autopartsestoresystem.models.entity.Model;
import com.myproject.autopartsestoresystem.vehicles.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


/**
 * @author Miroslav Kološnjaji
 */
@WebMvcTest(controllers = {VehicleController.class})
@MockBean({VehicleService.class})
@AutoConfigureMockMvc(addFilters = false)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleService vehicleService;

    private ObjectMapper objectMapper;
    private VehicleDTO vehicleDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        Model model = Model.builder()
                .id(1)
                .name("318")
                .brand(Brand.builder().id(1).name("BMW").models(new HashSet<>()).build())
                .build();


        vehicleDTO = VehicleDTO.builder()
                .parts(new ArrayList<>())
                .model(model)
                .engineType("2.0i Injection")
                .series("Series 3")
                .build();
    }

    @Disabled
    @DisplayName("Create Vehicle")
    @Test
    void testCreateVehicle_whenValidDetailsProvided_returns200StatusCode() throws Exception {

        //given
        when(vehicleService.save(vehicleDTO)).thenReturn(vehicleDTO);

        RequestBuilder requestBuilder = post(VehicleController.VEHICLE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        VehicleDTO savedVehicleDTO = objectMapper.readValue(response, VehicleDTO.class);

        //then
        assertNotNull(savedVehicleDTO, "Saved Vehicle DTO cannot be null");
        assertEquals(vehicleDTO, savedVehicleDTO, "Saved VehicleDTO did not match");
        verify(vehicleService).save(vehicleDTO);
    }

    @DisplayName("Create Vehicle Failed - Invalid Details Provided")
    @Test
    void testCreateVehicle_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        //given
        vehicleDTO.setModel(null);

        RequestBuilder requestBuilder = post(VehicleController.VEHICLE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Update Vehicle")
    @Test
    void testUpdateVehicle_whenValidDetailsProvided_returns204StatusCode() throws Exception {

        //given
        vehicleDTO.setEngineType("222");
        when(vehicleService.update(anyLong(),any(VehicleDTO.class))).thenReturn(vehicleDTO);

        RequestBuilder requestBuilder = put(VehicleController.VEHICLE_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(vehicleService).update(anyLong(), any(VehicleDTO.class));

    }

    @DisplayName("Update Vehicle Failed - Invalid Details Provided")
    @Test
    void testUpdateVehicle_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        //given
        vehicleDTO.setModel(null);

        RequestBuilder requestBuilder = put(VehicleController.VEHICLE_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Get All Vehicles")
    @Test
    void testGetAllVehicles_whenListIsPopulated_returns200StatusCode() throws Exception {

        //given
        List<VehicleDTO> vehicleDTOList  = List.of(mock(VehicleDTO.class), mock(VehicleDTO.class));
        when(vehicleService.getAll()).thenReturn(vehicleDTOList);

        RequestBuilder requestBuilder = get(VehicleController.VEHICLE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTOList));


        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        int size = objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size();
        //then
        assertEquals(2, size, "Expected 2 elements returned");
        verify(vehicleService).getAll();
    }

    @DisplayName("Get All Vehicles When List Is Empty - Returns Status 204")
    @Test
    void testGetAllVehicles_whenListIsEmpty_returns204StatusCode() throws Exception {

        //given
        List<VehicleDTO> vehicleDTOList  = List.of();
        when(vehicleService.getAll()).thenReturn(vehicleDTOList);

        RequestBuilder requestBuilder = get(VehicleController.VEHICLE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTOList));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
    }

    @DisplayName("Get Vehicle By ID")
    @Test
    void testGetVehicleById_whenValidIdProvided_returns200StatusCode() throws Exception {

        //given
        vehicleDTO.setId(1L);
        when(vehicleService.getById(anyLong())).thenReturn(vehicleDTO);

        RequestBuilder requestBuilder = get(VehicleController.VEHICLE_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        VehicleDTO foundDTO = objectMapper.readValue(response, VehicleDTO.class);

        assertNotNull(foundDTO, "Found DTO cannot be null");
        assertEquals(vehicleDTO.getEngineType(), foundDTO.getEngineType(), "Founded DTO did not match");
        verify(vehicleService).getById(anyLong());
    }

    @DisplayName("Get Vehicle By ID Failed - Invalid ID Provided")
    @Test
    void TestVehicleById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        when(vehicleService.getById(anyLong())).thenThrow(VehicleNotFoundException.class);

        RequestBuilder requestBuilder = get(VehicleController.VEHICLE_URI_WITH_ID, 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        verify(vehicleService).getById(anyLong());
    }

    @DisplayName("Delete Vehicle")
    @Test
    void testDeleteVehicleById_whenValidIdProvided_returns204StatusCode() throws Exception {

        //given
        doNothing().when(vehicleService).delete(anyLong());

        RequestBuilder requestBuilder = delete(VehicleController.VEHICLE_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(vehicleService).delete(anyLong());
    }

    @Test
    void testDeleteVehicleById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

       doThrow(VehicleNotFoundException.class).when(vehicleService).delete(99L);

        RequestBuilder requestBuilder = delete(VehicleController.VEHICLE_URI_WITH_ID, 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");

    }
}