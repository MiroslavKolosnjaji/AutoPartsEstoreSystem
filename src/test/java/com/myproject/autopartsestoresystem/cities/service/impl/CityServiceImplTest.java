package com.myproject.autopartsestoresystem.cities.service.impl;

import com.myproject.autopartsestoresystem.cities.dto.CityDTO;
import com.myproject.autopartsestoresystem.cities.exception.CityAlreadyExistsException;
import com.myproject.autopartsestoresystem.cities.exception.CityNotFoundException;
import com.myproject.autopartsestoresystem.cities.mapper.CityMapper;
import com.myproject.autopartsestoresystem.cities.entity.City;
import com.myproject.autopartsestoresystem.cities.repository.CityRepository;
import com.myproject.autopartsestoresystem.cities.service.impl.CityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    CityMapper cityMapper;

    @InjectMocks
    private CityServiceImpl cityService;

    private CityDTO cityDTO;

    @BeforeEach
    void setUp() {
        cityDTO = CityDTO.builder()
                .name("New York")
                .zipCode("12345")
                .build();
    }

    @DisplayName("Save City")
    @Test
    void testSaveCity_whenValidDetailsProvided_returnsCityDTO() throws CityAlreadyExistsException {

        //given
        City city = mock(City.class);
        when(cityMapper.cityDTOToCity(cityDTO)).thenReturn(city);
        when(cityMapper.cityToCityDTO(city)).thenReturn(cityDTO);

        when(cityRepository.save(city)).thenReturn(city);

        //when
        CityDTO savedDTO = cityService.save(cityDTO);

        //then
        assertNotNull(savedDTO);
        assertEquals(cityDTO, savedDTO);

        verify(cityMapper).cityDTOToCity(cityDTO);
        verify(cityMapper).cityToCityDTO(city);
        verify(cityRepository).save(city);
    }

    @DisplayName("Save City - City Already Exists - Throws CityAlreadyExistsException")
    @Test
    void testSaveCity_whenCityAlreadyExists_throwsCityAlreadyExistsException() {

        //given
        City city = mock(City.class);
        when(cityRepository.findByNameAndZipCode(anyString(), anyString())).thenReturn(Optional.of(city));

        //when
        Executable executable = () -> cityService.save(cityDTO);

        //then
        assertThrows(CityAlreadyExistsException.class, executable, "Exception not match");
        verify(cityRepository).findByNameAndZipCode(anyString(), anyString());
    }

    @DisplayName("Update City")
    @Test
    void testUpdateCity_whenValidDetailsProvided_returnsUpdatedDTO() throws CityNotFoundException {

        //given
        City city = mock(City.class);
        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(city));
        when(cityMapper.cityToCityDTO(city)).thenReturn(cityDTO);

        when(cityRepository.save(city)).thenReturn(city);

        //when
        CityDTO updatedDTO = cityService.update(anyLong(), cityDTO);

        //then
        assertNotNull(updatedDTO, "Updated city should not be null");
        assertEquals(cityDTO, updatedDTO);

        verify(cityRepository).findById(anyLong());
        verify(cityMapper).cityToCityDTO(city);
        verify(cityRepository).save(city);
    }


    @DisplayName("Update City - Wrong ID - Throws CityNotFoundException")
    @Test
    void testUpdateCity_whenInvalidIsProvided_throwsCityNotFoundException() {

        //given
        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> cityService.update(anyLong(), cityDTO);

        //then
        assertThrows(CityNotFoundException.class, executable, "Exception not match. Expected CityNotFoundException");

        verify(cityRepository).findById(anyLong());
    }

    @DisplayName("Get All Cities")
    @Test
    void testGetAll_whenListIsNotEmpty_returnListOfCityDTO() {

        //given
        List<City> cities = Arrays.asList(mock(City.class), mock(City.class), mock(City.class));
        when(cityRepository.findAll()).thenReturn(cities);

        //when
        List<CityDTO> citiesDTOList = cityService.getAll();

        assertNotNull(citiesDTOList, "List of Cities should not be null");
        assertFalse(citiesDTOList.isEmpty(), "List of Cities should not be empty");
        assertEquals(3, citiesDTOList.size(), "City list size is not equal to 3");

        verify(cityRepository).findAll();
    }

    @DisplayName("Get Cities - Empty List")
    @Test
    void testGetAll_whenListIsEmpty_returnsEmptyList() {

        //given
        List<City> cities = new ArrayList<>();
        when(cityRepository.findAll()).thenReturn(cities);

        //when
        List<CityDTO> citiesDTOList = cityService.getAll();

        //then
        assertNotNull(citiesDTOList, "List of Cities should not be null");
        assertTrue(citiesDTOList.isEmpty(), "List of Cities should be empty");

        verify(cityRepository).findAll();
    }

    @DisplayName("Get City By ID")
    @Test
    void testGetCity_whenValidIdProvided_returnsCityDTO() throws CityNotFoundException {

        //given
        City city = City.builder()
                .id(cityDTO.getId())
                .name(cityDTO.getName())
                .zipCode(cityDTO.getZipCode())
                .build();

        when(cityRepository.findById(anyLong())).thenReturn(Optional.of(city));
        when(cityMapper.cityToCityDTO(city)).thenReturn(cityDTO);

        //when
        CityDTO foundCityDTO = cityService.getById(anyLong());

        //then
        assertNotNull(foundCityDTO, "foundCityDTO should not be null");
        assertAll("Validate found city details",
                () -> assertEquals(cityDTO.getId(), foundCityDTO.getId(), "Id doesn't match"),
                () -> assertEquals(cityDTO.getName(), foundCityDTO.getName(), "Name doesn't match"),
                () -> assertEquals(cityDTO.getZipCode(), foundCityDTO.getZipCode(), "ZipCode doesn't match"));

        verify(cityRepository).findById(anyLong());
        verify(cityMapper).cityToCityDTO(any(City.class));

    }

    @DisplayName("Get City - Invalid ID - Throws CityNotFound Exception")
    @Test
    void testGetCity_whenInvalidIdProvided_throwsCityNotFoundException() {

        //given
        when(cityRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> cityService.getById(anyLong());

        //then
        assertThrows(CityNotFoundException.class, executable);
        verify(cityRepository).findById(anyLong());
    }

    @DisplayName("Delete City")
    @Test
    void testDeleteCity_whenValidIdProvided_thenSuccess() throws CityNotFoundException {

        //given
        City city = mock(City.class);
        when(cityRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(cityRepository).deleteById(anyLong());

        //when
        cityService.delete(anyLong());

        //then
        verify(cityRepository).existsById(anyLong());
        verify(cityRepository).deleteById(anyLong());
    }

    @DisplayName("Delete City - Invalid ID - throws CityNotFoundException")
    @Test
    void testDeleteCity_whenInvalidIdProvided_throwsCityNotFoundException() {

        //given
        when(cityRepository.existsById(anyLong())).thenReturn(false);

        //when
        Executable executable = () -> cityService.delete(anyLong());

        //then
        assertThrows(CityNotFoundException.class, executable);
        verify(cityRepository).existsById(anyLong());
    }
}
