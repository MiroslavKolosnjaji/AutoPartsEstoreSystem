package com.myproject.autopartsestoresystem.parts.service.impl;

import com.myproject.autopartsestoresystem.parts.dto.PartGroupDTO;
import com.myproject.autopartsestoresystem.parts.exception.PartGroupAlreadyExistsException;
import com.myproject.autopartsestoresystem.parts.exception.PartGroupNotFoundException;
import com.myproject.autopartsestoresystem.parts.mapper.PartGroupMapper;
import com.myproject.autopartsestoresystem.parts.entity.PartGroup;
import com.myproject.autopartsestoresystem.parts.entity.PartGroupType;
import com.myproject.autopartsestoresystem.parts.repository.PartGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class PartGroupServiceImplTest {

    @Mock
    private PartGroupRepository partGroupRepository;

    @Mock
    private PartGroupMapper partGroupMapper;

    @InjectMocks
    private PartGroupServiceImpl partGroupService;

    private PartGroupDTO partGroupDTO;

    private PartGroup partGroup;

    @BeforeEach
    void setUp() {
        partGroupDTO = PartGroupDTO.builder()
                .id(1L)
                .name(PartGroupType.BRAKING_SYSTEM)
                .parts(new ArrayList<>())
                .build();

        partGroup = PartGroup.builder()
                .id(1L)
                .name(PartGroupType.BRAKING_SYSTEM)
                .parts(new ArrayList<>())
                .build();

    }

    @DisplayName("Save Part Group")
    @Test
    void testSavePartGroup_whenValidDetailsProvided_returnsPartGroupDTO() throws PartGroupAlreadyExistsException {

        //given
        when(partGroupRepository.findByName(partGroupDTO.getName())).thenReturn(Optional.empty());
        when(partGroupMapper.partGroupDTOToPartGroup(partGroupDTO)).thenReturn(partGroup);
        when(partGroupMapper.partGroupToPartGroupDTO(any(PartGroup.class))).thenReturn(partGroupDTO);

        when(partGroupRepository.save(partGroup)).thenReturn(partGroup);

        //when
        PartGroupDTO savedDTO = partGroupService.save(partGroupDTO);

        //then
        assertNotNull(savedDTO, "Saved part group should not be null");
        assertEquals(partGroupDTO, savedDTO, "Saved DTO should be equal to partGroupDTO");

        verify(partGroupRepository).findByName(partGroupDTO.getName());
        verify(partGroupMapper).partGroupDTOToPartGroup(partGroupDTO);
        verify(partGroupMapper).partGroupToPartGroupDTO(any(PartGroup.class));
        verify(partGroupRepository).save(partGroup);
    }

    @DisplayName("Save Part Group - Part Group Already Exists - Throws PartGroupAlreadyExistsException")
    @Test
    void testSavePartGroup_whenPartAlreadyExists_throwsPartGroupAlreadyExistsException() {

        //given
        when(partGroupRepository.findByName(partGroupDTO.getName())).thenReturn(Optional.of(partGroup));

        //when
        Executable executable = () -> partGroupService.save(partGroupDTO);

        //then
        assertThrows(PartGroupAlreadyExistsException.class, executable, "Exception doesn't match");
    }

    @DisplayName("Update Part Group")
    @Test
    void testUpdatePartGroup_whenValidDetailsProvided_returnsPartGroupDTO() throws PartGroupNotFoundException {

        //given
        partGroupDTO.setName(PartGroupType.FUEL_SYSTEM);

        when(partGroupRepository.findById(anyLong())).thenReturn(Optional.of(partGroup));
        when(partGroupMapper.partGroupToPartGroupDTO(partGroup)).thenReturn(partGroupDTO);

        when(partGroupRepository.save(partGroup)).thenReturn(partGroup);

        //when
        PartGroupDTO updatedDTO = partGroupService.update(anyLong(), partGroupDTO);

        assertNotNull(updatedDTO, "Updated DTO should not be null");
        assertEquals(partGroupDTO, updatedDTO, "Updated DTO should be equal to partGroupDTO");

        verify(partGroupRepository).findById(anyLong());
        verify(partGroupMapper).partGroupToPartGroupDTO(partGroup);
        verify(partGroupRepository).save(partGroup);
    }

    @DisplayName("Update Part Group Failed - Invalid ID Provided")
    @Test
    void testUpdatePartGroup_whenInvalidIdProvided_throwsPartGroupDoesntExistsException() {

        //given
        when(partGroupRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> partGroupService.update(anyLong(), partGroupDTO);

        assertThrows(PartGroupNotFoundException.class, executable, "Exception doesn't match. Expected PartGroupNotFoundException");
        verify(partGroupRepository).findById(anyLong());
    }

    @DisplayName("Get All Part Groups")
    @Test
    void testGetAllPartGroups_whenListIsPopulated_returnsListOfPartGroupDTO() {

        //given
        List<PartGroup> partGroups = List.of(mock(PartGroup.class), mock(PartGroup.class), mock(PartGroup.class));
        when(partGroupRepository.findAll()).thenReturn(partGroups);

        //when
        List<PartGroupDTO> partGroupDTOList = partGroupService.getAll();

        //then
        assertNotNull(partGroupDTOList, "List should not be null");
        assertFalse(partGroupDTOList.isEmpty(), "List should not be empty");
        assertEquals(partGroupDTOList.size(), partGroups.size(), "List should have 3 elements");

        verify(partGroupRepository).findAll();
    }

    @DisplayName("Get All Part Groups - Empty List")
    @Test
    void testGetAllPartGroups_whenListIsEmpty_returnsEmptyList() {

        //given
        List<PartGroup> partGroups = List.of();
        when(partGroupRepository.findAll()).thenReturn(partGroups);

        //when
        List<PartGroupDTO> partGroupDTOList = partGroupService.getAll();


        assertNotNull(partGroupDTOList, "List should not be null");
        assertTrue(partGroupDTOList.isEmpty(), "List should be empty");

        verify(partGroupRepository).findAll();
    }

    @DisplayName("Get Part Group By ID")
    @Test
    void testGetPartGroupById_whenValidIdProvided_returnsPartGroupDTO() throws PartGroupNotFoundException {

        //given
        when(partGroupRepository.findById(anyLong())).thenReturn(Optional.of(partGroup));
        when(partGroupMapper.partGroupToPartGroupDTO(partGroup)).thenReturn(partGroupDTO);

        //when
        PartGroupDTO foundDTO = partGroupService.getById(anyLong());

        //then
        assertNotNull(foundDTO, "Found Part group should not be null");
        verify(partGroupRepository).findById(anyLong());
        verify(partGroupMapper).partGroupToPartGroupDTO(partGroup);
    }

    @DisplayName("Get Part Group By ID")
    @Test
    void testGetPartGroupById_whenInvalidIdProvided_throwsPartGroupNotFoundException() {

        //given
        when(partGroupRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> partGroupService.getById(anyLong());

        //then
        assertThrows(PartGroupNotFoundException.class, executable, "Exception doesn't match. Expected PartGroupNotFoundException");
        verify(partGroupRepository).findById(anyLong());
    }

    @DisplayName("Delete Part Group")
    @Test
    void testDeletePartGroup_whenValidIDProvided_thenSuccess() throws PartGroupNotFoundException {

        //given
        when(partGroupRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(partGroupRepository).deleteById(anyLong());

        //when
        partGroupService.delete(anyLong());

        //then
        verify(partGroupRepository).existsById(anyLong());
        verify(partGroupRepository).deleteById(anyLong());
    }

    @DisplayName("Delete Part Group Failed - Invalid ID Provided")
    @Test
    void testDeletePartGroup_whenInvalidIDProvided_throwsPartGroupNotFOundException() {

        //given
        when(partGroupRepository.existsById(anyLong())).thenReturn(false);

        //when
        Executable executable = () -> partGroupService.delete(anyLong());

        //then
        assertThrows(PartGroupNotFoundException.class, executable, "Exception doesn't match. Expected PartGroupNotFoundException");
    }
}