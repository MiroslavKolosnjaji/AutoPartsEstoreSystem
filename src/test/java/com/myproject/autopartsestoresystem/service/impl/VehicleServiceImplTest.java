package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.VehicleDTO;
import com.myproject.autopartsestoresystem.exception.service.VehicleNotFoundException;
import com.myproject.autopartsestoresystem.mapper.VehicleMapper;
import com.myproject.autopartsestoresystem.brands.entity.Brand;
import com.myproject.autopartsestoresystem.model.Model;
import com.myproject.autopartsestoresystem.model.ModelId;
import com.myproject.autopartsestoresystem.model.Vehicle;
import com.myproject.autopartsestoresystem.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

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

    @DisplayName("Save Vehicle")
    @Test
    void testSaveVehicle_whenValidDetailsProvided_returnsVehicleDTO() {

        //given

        Vehicle vehicle = mock(Vehicle.class);

        when(vehicleMapper.vehicleToVehicleDTO(vehicle)).thenReturn(vehicleDTO);
        when(vehicleMapper.vehicleDTOToVehicle(vehicleDTO)).thenReturn(vehicle);

        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        //when
        VehicleDTO savedDTO = vehicleService.save(vehicleDTO);

        //then
        assertNotNull(savedDTO, "Saved vehicle should not be null");
        assertEquals(vehicleDTO, savedDTO, "Saved vehicle should be equal to vehicleDTO");

        verify(vehicleRepository).save(vehicle);
        verify(vehicleMapper).vehicleToVehicleDTO(vehicle);
        verify(vehicleMapper).vehicleDTOToVehicle(vehicleDTO);
    }

    @DisplayName("Update Vehicle")
    @Test
    void testUpdateVehicle_whenValidDetailsProvided_returnsVehicleDTO() throws VehicleNotFoundException {

        //given
        Vehicle vehicle = mock(Vehicle.class);

        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.vehicleToVehicleDTO(vehicle)).thenReturn(vehicleDTO);
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        //when
        VehicleDTO updatedDTO = vehicleService.update(anyLong(), vehicleDTO);

        //then
        assertNotNull(updatedDTO, "Updated vehicle should not be null");
        assertEquals(vehicleDTO, updatedDTO, "Updated vehicle should be equal to vehicleDTO");

        verify(vehicleRepository).findById(anyLong());
        verify(vehicleMapper).vehicleToVehicleDTO(vehicle);
        verify(vehicleRepository).save(vehicle);
    }

    @DisplayName("Update Vehicle - Failed - Throws VehicleNotFoundException")
    @Test
    void testUpdateVehicle_whenVehicleIdNotFound_throwsVehicleNotFoundException() {

        //given
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> vehicleService.update(anyLong(), vehicleDTO);

        //then
        assertThrows(VehicleNotFoundException.class, executable, "Exception doesn't match. Expected VehicleNotFoundException");
        verify(vehicleRepository).findById(anyLong());
    }

    @DisplayName("Get All Vehicles")
    @Test
    void testGetAllVehicles_whenListIsPopulated_returnsListOfVehicleDTO() {

        //given
        List<Vehicle> vehicleList = List.of(mock(Vehicle.class), mock(Vehicle.class));
        when(vehicleRepository.findAll()).thenReturn(vehicleList);

        //when
        List<VehicleDTO> getAll = vehicleService.getAll();

        //then
        assertNotNull(getAll, "getAll should not be null");
        verify(vehicleRepository).findAll();
    }

    @DisplayName("Get Vehicle By ID")
    @Test
    void testGetVehicleById_whenValidIdProvided_returnsVehicleDTO() throws VehicleNotFoundException {

        //given
        Vehicle vehicle = mock(Vehicle.class);
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.of(vehicle));
        when(vehicleMapper.vehicleToVehicleDTO(vehicle)).thenReturn(vehicleDTO);

        //when
        VehicleDTO foundDTO = vehicleService.getById(anyLong());

        //then
        assertNotNull(foundDTO, "Found vehicle should not be null");
        assertEquals(vehicleDTO, foundDTO, "Found vehicle should be equal to vehicleDTO");

        verify(vehicleRepository).findById(anyLong());
        verify(vehicleMapper).vehicleToVehicleDTO(vehicle);
    }

    @DisplayName("Get Vehicle By ID - Failed - Throws VehicleNotFoundException")
    @Test
    void testGetVehicleById_whenInvalidIdProvided_throwsVehicleNotFoundException() {

        //given
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> vehicleService.getById(anyLong());

        //then
        assertThrows(VehicleNotFoundException.class, executable, "Exception doesn't match. Expected VehicleNotFoundException");
    }

    @DisplayName("Delete Vehicle")
    @Test
    void testDeleteVehicle_whenValidIdProvided_thenCorrect() throws VehicleNotFoundException {

        //given
        when(vehicleRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(vehicleRepository).deleteById(anyLong());

        //when
        vehicleService.delete(anyLong());

        //then
        verify(vehicleRepository).deleteById(anyLong());
        verify(vehicleRepository).existsById(anyLong());
    }

    @DisplayName("Delete Vehicle - Failed - Throws VehicleNotFoundException")
    @Test
    void testDeleteVehicle_whenInvalidIdProvided_throwsVehicleNotFoundException() {

        //given
        when(vehicleRepository.existsById(anyLong())).thenReturn(false);

        //when
        Executable executable = () -> vehicleService.delete(anyLong());

        //then
        assertThrows(VehicleNotFoundException.class, executable, "Exception doesn't match. Expected VehicleNotFoundException");
    }
}