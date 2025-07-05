package com.myproject.autopartsestoresystem.stores.service.impl;

import com.myproject.autopartsestoresystem.stores.dto.StoreDTO;
import com.myproject.autopartsestoresystem.stores.exception.StoreNotFoundException;
import com.myproject.autopartsestoresystem.stores.mapper.StoreMapper;
import com.myproject.autopartsestoresystem.cities.entity.City;
import com.myproject.autopartsestoresystem.stores.entity.Store;
import com.myproject.autopartsestoresystem.stores.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class StoreServiceImplTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreMapper storeMapper;

    @InjectMocks
    private StoreServiceImpl storeService;

    private StoreDTO storeDTO;

    @BeforeEach
    void setUp() {
        storeDTO = StoreDTO.builder()
                .name("Store XYZ")
                .email("xyz@example.com")
                .phoneNumber("123123123")
                .city(City.builder().build())
                .build();
    }


    @DisplayName("Save Store")
    @Test
    void testSaveStore_whenValidDetailsProvided_returnsStoreSTO() {

        //given
        Store store = mock(Store.class);

        when(storeMapper.storeDTOToStore(storeDTO)).thenReturn(store);
        when(storeMapper.storeToStoreDTO(store)).thenReturn(storeDTO);
        when(storeRepository.save(store)).thenReturn(store);

        //when
        StoreDTO savedDTO = storeService.save(storeDTO);

        //then
        assertNotNull(savedDTO, "Saved Store should not be null");
        assertEquals(storeDTO, savedDTO, "Saved Store should be the same");

        verify(storeMapper).storeDTOToStore(storeDTO);
        verify(storeMapper).storeToStoreDTO(store);
        verify(storeRepository).save(store);
    }

    @DisplayName("Update Store")
    @Test
    void testUpdateStore_whenValidDetailsProvided_returnsStoreDTO() throws StoreNotFoundException {

        //given
        Store store = Store.builder()
                .name(storeDTO.getName())
                .email(storeDTO.getEmail())
                .phoneNumber(storeDTO.getPhoneNumber())
                .city(storeDTO.getCity())
                .build();

        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
        when(storeMapper.storeToStoreDTO(store)).thenReturn(storeDTO);
        when(storeRepository.save(store)).thenReturn(store);

        //when
        StoreDTO updatedDTO = storeService.update(anyLong(), storeDTO);

        //then
        assertNotNull(updatedDTO, "Updated Store should not be null");
        assertEquals(storeDTO, updatedDTO, "Updated Store should be the same");

        verify(storeRepository).findById(anyLong());
        verify(storeMapper).storeToStoreDTO(store);
        verify(storeRepository).save(store);
    }

    @DisplayName("Update Store Failed - Throws StoreNotFoundException")
    @Test
    void testUpdateStore_whenStoreIDNotFound_throwsStoreNotFoundException() {

        //given
        when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> storeService.update(anyLong(), storeDTO);

        //then
        assertThrows(StoreNotFoundException.class, executable, "Exception doesn't match. Expected StoreNotFoundException");
        verify(storeRepository).findById(anyLong());
    }

    @DisplayName("Get All Stores")
    @Test
    void testGetAllStores_whenListIsPopulated_returnsListOfStoreDTO() {

        //given
        List<Store> storeList = List.of(mock(Store.class), mock(Store.class));
        when(storeRepository.findAll()).thenReturn(storeList);

        //when
        List<StoreDTO> storeDTOList = storeService.getAll();

        //then
        assertNotNull(storeDTOList, "List should not be null");
        assertEquals(storeDTOList.size(), storeList.size(), "List should have the same size");
        verify(storeRepository).findAll();
    }

    @DisplayName("Get Store By ID")
    @Test
    void testGetStoreByID_whenValidIDProvided_returnsStoreDTO() throws StoreNotFoundException {

        //given
        Store store = Store.builder()
                .name(storeDTO.getName())
                .email(storeDTO.getEmail())
                .phoneNumber(storeDTO.getPhoneNumber())
                .city(storeDTO.getCity())
                .build();

        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
        when(storeMapper.storeToStoreDTO(store)).thenReturn(storeDTO);

        //when
        StoreDTO foundDTO = storeService.getById(anyLong());

        //then
        assertNotNull(foundDTO, "Store should not be null");
        assertEquals(storeDTO, foundDTO, "Store should be the same");
        verify(storeRepository).findById(anyLong());
        verify(storeMapper).storeToStoreDTO(store);

    }

    @DisplayName("Get Store By ID Failed - Invalid ID Provided")
    @Test
    void testGetStoreByID_WhenInvalidIDProvided_throwsStoreNotFoundException() {

        //given
        when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> storeService.getById(anyLong());

        //then
        assertThrows(StoreNotFoundException.class, executable, "Exception doesn't match. Expected StoreNotFoundException");
        verify(storeRepository).findById(anyLong());
    }

    @DisplayName("Delete Store")
    @Test
    void testDeleteStoreByID_whenValidIDProvided_thenCorrect() throws StoreNotFoundException {

        //given
        Store store = mock(Store.class);
        when(storeRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(storeRepository).deleteById(anyLong());

        //when
        storeService.delete(anyLong());

        //then
        verify(storeRepository).deleteById(anyLong());
        verify(storeRepository).existsById(anyLong());

    }

    @DisplayName("Delete Store Failed - Invalid ID Provided")
    @Test
    void testDeleteStoreByID_whenInvalidIDProvided_throwsStoreNotFoundException() {

        //given
        when(storeRepository.existsById(anyLong())).thenReturn(false);

        //when
        Executable executable = () -> storeService.delete(anyLong());

        //then
        assertThrows(StoreNotFoundException.class, executable, "Exception doesn't match. Expected StoreNotFoundException");
        verify(storeRepository).existsById(anyLong());
    }
}