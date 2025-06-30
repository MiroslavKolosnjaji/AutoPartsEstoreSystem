package com.myproject.autopartsestoresystem.brands.service;

import com.myproject.autopartsestoresystem.brands.dto.BrandDTO;
import com.myproject.autopartsestoresystem.brands.exception.BrandAlreadyExistsException;
import com.myproject.autopartsestoresystem.brands.exception.BrandNotFoundException;
import com.myproject.autopartsestoresystem.brands.mapper.BrandMapper;
import com.myproject.autopartsestoresystem.brands.entity.Brand;
import com.myproject.autopartsestoresystem.brands.repository.BrandRepository;
import com.myproject.autopartsestoresystem.brands.service.impl.BrandServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    private BrandDTO brandDTO;

    @BeforeEach
    void setUp() {
        brandDTO = BrandDTO.builder().id(1L).name("AUDI").build();
    }

    @DisplayName("Save Brand")
    @Test
    void testSaveBrand_whenValidInputProvided_returnsSavedBrandDTO() throws BrandAlreadyExistsException {

        //given
        Brand brand = mock(Brand.class);
        when(brandRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(brandMapper.brandToBrandDTO(brand)).thenReturn(brandDTO);
        when(brandMapper.brandDTOToBrand(brandDTO)).thenReturn(brand);

        when(brandRepository.save(brand)).thenReturn(brand);

        //when
        BrandDTO savedDTO = brandService.save(brandDTO);

        //then
        assertNotNull(savedDTO);
        assertEquals(brandDTO, savedDTO);

        verify(brandRepository).findByName(anyString());
        verify(brandMapper).brandToBrandDTO(brand);
        verify(brandMapper).brandDTOToBrand(brandDTO);
        verify(brandRepository).save(brand);
    }
    
    @DisplayName("Save Brand Failed - Brand Already Exists")
    @Test
    void testSaveBrand_whenBrandAlreadyExists_throwsBrandAlreadyExistsException() {

        //given
        Brand brand = mock(Brand.class);
        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(brand));

        //when
        Executable executable = () -> brandService.save(brandDTO);

        //then
        assertThrows(BrandAlreadyExistsException.class, executable, "Exception mismatch");
    }

    @DisplayName("Update Brand")
    @Test
    void testUpdateBrand_whenValidDetailsProvided_returnsUpdatedBrandDTO() throws BrandNotFoundException {

        //given
        Brand brand = mock(Brand.class);
        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(brand));
        when(brandMapper.brandToBrandDTO(brand)).thenReturn(brandDTO);

        when(brandRepository.save(brand)).thenReturn(brand);

        //when
        BrandDTO updatedDTO = brandService.update(anyLong(), brandDTO);

        //then
        assertNotNull(updatedDTO);
        assertEquals(brandDTO, updatedDTO);

        verify(brandRepository).findById(anyLong());
        verify(brandMapper).brandToBrandDTO(brand);
        verify(brandRepository).save(brand);
    }

    @DisplayName("Update Brand Failed - Invalid ID Provided")
    @Test
    void testUpdateBrand_whenBrandIdNotFound_throwsBrandNotFoundExistsException() {

        //given
        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> brandService.update(anyLong(), brandDTO);

        //then
        assertThrows(BrandNotFoundException.class, executable, "Exception mismatch");
    }

    @Test
    void testGetAllBrands_whenListIsPopulated_returnsListOfBrandDTO() {

        //given
        List<Brand> brands = List.of(mock(Brand.class), mock(Brand.class));
        when(brandRepository.findAll()).thenReturn(brands);

        //when
        List<BrandDTO> brandDTOList = brandService.getAll();

        //then
        assertNotNull(brandDTOList);
        assertEquals(brands.size(), brandDTOList.size());

        verify(brandRepository).findAll();
    }

    @DisplayName("Get Brand By ID")
    @Test
    void testGetBrandById_whenValidIdProvided_returnsBrandDTO() throws BrandNotFoundException {

        //given
        Brand brand = mock(Brand.class);
        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(brand));
        when(brandMapper.brandToBrandDTO(brand)).thenReturn(brandDTO);

        //when
        BrandDTO foundDTO = brandService.getById(brandDTO.getId());

        //then
        assertNotNull(foundDTO);
        assertEquals(brandDTO, foundDTO);

        verify(brandRepository).findById(anyLong());
        verify(brandMapper).brandToBrandDTO(brand);
    }

    @DisplayName("Get Brand By ID Failed - Invalid ID Provided")
    @Test
    void testGetBrandById_whenInvalidIdProvided_throwsBrandNotFoundException() {

        //given
        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> brandService.getById(brandDTO.getId());

        //then
        assertThrows(BrandNotFoundException.class, executable, "Exception mismatch");
    }

    @DisplayName("Delete Brand")
    @Test
    void testDeleteBrandById_whenValidIdProvided_thenCorrect() throws BrandNotFoundException {

        //given
        when(brandRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(brandRepository).deleteById(anyLong());

        //when
        brandService.delete(brandDTO.getId());

        //then
        verify(brandRepository).deleteById(anyLong());
        verify(brandRepository).existsById(anyLong());
    }

    @Test
    void testDeleteBrandById_whenIvalidIdProvided_throwsBrandNotFoundException() {

        //given
        when(brandRepository.existsById(anyLong())).thenReturn(false);

        //when
        Executable executable = () -> brandService.delete(brandDTO.getId());

        //then
        assertThrows(BrandNotFoundException.class, executable, "Exception mismatch");
    }
}