package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.VehicleDTO;
import com.myproject.autopartsestoresystem.exception.service.VehicleNotFoundException;
import com.myproject.autopartsestoresystem.model.Brand;
import com.myproject.autopartsestoresystem.model.Model;
import com.myproject.autopartsestoresystem.model.ModelId;
import com.myproject.autopartsestoresystem.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
                .id(new ModelId(1L, "318"))
                .brand(Brand.builder().id(1L).name("BMW").models(new HashSet<>()).build())
                .build();


        vehicleDTO = VehicleDTO.builder()
                .brand(model.getBrand())
                .parts(new ArrayList<>())
                .model(model)
                .engineType("2.0i Injection")
                .series("Series 3")
                .build();
    }

    @Disabled("Create Vehicle")
    @Test
    void testCreateVehicle_whenValidDetailsProvided_returnsCreatedVehicleDTO() throws Exception {

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
        assertNotNull(savedVehicleDTO, "Save Vehicle DTO cannot be null");
        assertEquals(vehicleDTO, savedVehicleDTO, "Save Vehicle DTO did not match");
        verify(vehicleService).save(vehicleDTO);
    }

    @DisplayName("Create Vehicle  When Ivalid Details Provided -Returns Status 400")
    @Test
    void testCreateVehicle_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        //given
        vehicleDTO.setBrand(null);

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

    @DisplayName("Update Vehicle When Invalid Details Provided - Returns Status 400")
    @Test
    void testUpdateVehicle_whenInvalidDetailsProvided_returns404StatusCode() throws Exception {

        //given
        vehicleDTO.setBrand(null);

        RequestBuilder requestBuilder = put(VehicleController.VEHICLE_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
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

        //then
        assertEquals(2, objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size(), "Expected list of 2 objects returned");
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
        VehicleDTO foundedDTO = objectMapper.readValue(response, VehicleDTO.class);

        assertNotNull(foundedDTO, "Founded DTO cannot be null");
        assertEquals(vehicleDTO, foundedDTO, "Founded DTO did not match");
        verify(vehicleService).getById(anyLong());
    }

    @DisplayName("Get Vehicle By ID When Invalid ID Provided - Returns 404 Status Code")
    @Test
    void TestVehicleById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        when(vehicleService.getById(anyLong())).thenThrow(VehicleNotFoundException.class);

        RequestBuilder requestBuilder = get(VehicleController.VEHICLE_URI_WITH_ID, 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDTO));

        //
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