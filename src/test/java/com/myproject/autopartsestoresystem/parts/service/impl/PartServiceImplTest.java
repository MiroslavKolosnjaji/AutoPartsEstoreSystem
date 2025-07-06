package com.myproject.autopartsestoresystem.parts.service.impl;

import com.myproject.autopartsestoresystem.parts.dto.PartDTO;
import com.myproject.autopartsestoresystem.parts.dto.PriceDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.parts.entity.*;
import com.myproject.autopartsestoresystem.parts.exception.PartNotFoundException;
import com.myproject.autopartsestoresystem.parts.mapper.PartMapper;
import com.myproject.autopartsestoresystem.parts.repository.PartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
class PartServiceImplTest {

    @Mock
    private PartRepository partRepository;

    @Mock
    private PartMapper partMapper;

    @InjectMocks
    private PartServiceImpl partService;

    private Part part;

    private PartDTO partDTO;

    private PriceDTO priceDTO;

    private List<Price> prices;


    @BeforeEach
    void setUp() {

        priceDTO = PriceDTO.builder()
                .id(new PriceId(1L, 0L))
                .price(new BigDecimal("69.99"))
                .currency(Currency.USD)
                .dateModified(LocalDateTime.now())
                .build();

        prices = new ArrayList<>();
        prices.add(Price.builder().id(new PriceId(1L, 0L))
                .price(new BigDecimal("69.99"))
                .currency(Currency.USD)
                .dateModified(LocalDateTime.now())
                .build());

        part = Part.builder().id(1L)
                .partNumber("BR56789")
                .partName("Rear Brake Rotor")
                .description("High-quality rear brake rotor designed for optimal heat dissapation and minimal wear. Ensures smooth and effective braking perfomance")
                .prices(prices)
                .partGroup(PartGroup.builder().id(1L).name(PartGroupType.BRAKING_SYSTEM).parts(new ArrayList<>()).build())
                .build();


        partDTO = PartDTO.builder()
                .id(1L)
                .partNumber("BR56789")
                .partName("Rear Brake Rotor")
                .description("High-quality rear brake rotor designed for optimal heat dissapation and minimal wear. Ensures smooth and effective braking perfomance")
                .prices(prices)
                .partGroup(PartGroup.builder().id(1L).name(PartGroupType.BRAKING_SYSTEM).parts(new ArrayList<>()).build())
                .build();

    }


    @DisplayName("Save Part")
    @Test
    void testSavePart_whenValidDetailsProvided_returnPartDTO() throws EntityAlreadyExistsException {

        //given
        when(partMapper.partDTOToPart(partDTO)).thenReturn(part);
        when(partMapper.partToPartDTO(part)).thenReturn(partDTO);

        when(partRepository.save(part)).thenReturn(part);

        //when
        PartDTO savedDTO = partService.save(partDTO);

        //then
        assertNotNull(savedDTO, "Saved DTO cannot be null");
        assertEquals(partDTO, savedDTO, "Saved DTO is not equal to PartDTO");

        verify(partMapper).partToPartDTO(part);
        verify(partMapper).partDTOToPart(partDTO);
        verify(partRepository).save(part);

    }

    @DisplayName("Update Part - Update Existing Price")
    @Test
    void testUpdatePart_whenValidDetailsProvided_returnUpdatedPartDTO() throws PartNotFoundException {

        //given
        priceDTO.setId(new PriceId(1L, 1L));
        when(partRepository.findById(anyLong())).thenReturn(Optional.of(part));
        when(partMapper.partToPartDTO(part)).thenReturn(partDTO);


        when(partRepository.save(part)).thenReturn(part);

        //when
        PartDTO updatedDTO = partService.update(part.getId(), partDTO);

        //then
        assertNotNull(updatedDTO, "Updated DTO cannot be null");
        assertEquals(partDTO, updatedDTO, "Updated DTO is not equal to PartDTO");

        verify(partMapper).partToPartDTO(part);
        verify(partRepository).save(part);

    }

    @DisplayName("Update City - Wrong ID - Throws PartNotFoundException")
    @Test
    void testUpdatePart_whenPartDoesntExistsInDataBase_throwsPartNotFoundException() {

        //given
        when(partRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> partService.update(part.getId(), partDTO);

        //then
        assertThrows(PartNotFoundException.class, executable, "Exception not match. Expected PartNotFoundException");
        verify(partRepository).findById(anyLong());
    }

    @DisplayName("Get Selected Parts By ID")
    @Test
    void testGetSelectedParts_whenValidIdsIsProvided_returnsListOfPartDto() throws PartNotFoundException {

        //given
        List<Long> ids = List.of(1L, 2L);
        List<Part> parts = List.of(mock(Part.class), mock(Part.class));
        List<PartDTO> partsDTO = List.of(mock(PartDTO.class), mock(PartDTO.class));

        when(partRepository.getSelectedParts(ids)).thenReturn(Optional.of(parts));
        when(partMapper.partsToPartDTOs(parts)).thenReturn(partsDTO);

        //when
        List<PartDTO> partDTOList = partService.getSelectedParts(ids);

        //then
        assertNotNull(partDTOList);
        assertEquals(parts.size(), partDTOList.size());

        verify(partRepository).getSelectedParts(ids);
        verify(partMapper).partsToPartDTOs(parts);
    }

    @DisplayName("Get Selected Parts Failed - Invalid IDs Provided")
    @Test
    void testGetSelectedParts_whenPartsNotFound_throwsPartNotFoundException() {

        //given
        when(partRepository.getSelectedParts(anyList())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> partService.getSelectedParts(List.of(1L));

        //then
        assertThrows(PartNotFoundException.class, executable, "Exception not match. Expected PartNotFoundException");
    }

    @DisplayName("Get All Parts")
    @Test
    void testGetAllParts_whenListIsPopulated_returnsListOfPartDTO() {

        //given
        List<Part> parts = Arrays.asList(mock(Part.class), mock(Part.class), mock(Part.class));
        when(partRepository.findAll()).thenReturn(parts);

        //when
        List<PartDTO> partDTOList = partService.getAll();

        //then
        assertNotNull(partDTOList, "Get All Parts cannot be null");
        assertFalse(partDTOList.isEmpty(), "Get All Parts cannot be empty");
        assertEquals(3, partDTOList.size(), "Get All Parts size is not equal to 3");

        verify(partRepository).findAll();
    }

    @DisplayName("Get Part By ID")
    @Test
    void testGetPartById_whenValidIdProvided_returnsPartDTO() throws PartNotFoundException {

        //given
        List<PriceDTO> pricedtos = Arrays.asList(mock(PriceDTO.class));

        when(partRepository.findById(anyLong())).thenReturn(Optional.of(part));
        when(partMapper.partToPartDTO(part)).thenReturn(partDTO);

        //when
        PartDTO foundPartDTO = partService.getById(anyLong());

        //then
        assertNotNull(foundPartDTO, "Get Part cannot be null");
        assertEquals(partDTO, foundPartDTO, "Get Part is not equal to PartDTO");
        assertEquals(pricedtos.size(), foundPartDTO.getPrices().size(), "Prices size is not equal to 2");

        verify(partRepository).findById(anyLong());
        verify(partMapper).partToPartDTO(part);
    }

    @DisplayName("Get Part By ID - Wrong ID provided - Throws PartNotFoundException")
    @Test
    void testGetPartByID_whenInvalidIdProvided_throwsPartNotFoundException() {

        //given
        when(partRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> partService.getById(anyLong());

        //then
        assertThrows(PartNotFoundException.class, executable, "Exception not match. Expected PartNotFoundException");
        verify(partRepository).findById(anyLong());
    }

    @DisplayName("Delete Part")
    @Test
    void testDeletePart_whenValidIdProvided_thenSuccess() throws PartNotFoundException {

        //given
        when(partRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(partRepository).deleteById(anyLong());

        //when
        partService.delete(anyLong());

        //then
        verify(partRepository).existsById(anyLong());
        verify(partRepository).deleteById(anyLong());
    }

    @DisplayName("Delete Part By ID - Invalid ID - throws PartNotFoundException")
    @Test
    void testDeletePart_whenInvalidIdProvided_throwsPartNotFoundException() {

        //given
        when(partRepository.existsById(anyLong())).thenReturn(false);

        //when
        Executable executable = () -> partService.delete(anyLong());

        //then
        assertThrows(PartNotFoundException.class, executable, "Exception not match. Expected PartNotFoundException");
    }
}