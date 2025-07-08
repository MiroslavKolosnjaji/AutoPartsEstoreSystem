package com.myproject.autopartsestoresystem.models.service.impl;

import com.myproject.autopartsestoresystem.models.dto.ModelDTO;
import com.myproject.autopartsestoresystem.models.exception.ModelAlreadyExistsException;
import com.myproject.autopartsestoresystem.models.exception.ModelNotFoundException;
import com.myproject.autopartsestoresystem.models.mapper.ModelMapper;
import com.myproject.autopartsestoresystem.brands.entity.Brand;
import com.myproject.autopartsestoresystem.models.entity.Model;
import com.myproject.autopartsestoresystem.models.repository.ModelRepository;
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
class ModelServiceImplTest {

    @Mock
    private ModelRepository modelRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ModelServiceImpl modelService;

    private ModelDTO modelDTO;

    @BeforeEach
    void setUp() {
        modelDTO = ModelDTO.builder()
                .id(1)
                .name("330")
                .brand(new Brand(1, "BMW", null))
                .build();
    }

    @DisplayName("Save Model")
    @Test
    void testSaveModel_whenValidDetailsProvided_returnsModelDTO() throws ModelAlreadyExistsException {

        //given
        Model model = mock(Model.class);
        when(modelMapper.toEntity(modelDTO)).thenReturn(model);
        when(modelMapper.toDto(model)).thenReturn(modelDTO);

        when(modelRepository.save(model)).thenReturn(model);

        //when
        ModelDTO savedDTO = modelService.save(modelDTO);

        //then
        assertNotNull(savedDTO);
        assertEquals(modelDTO, savedDTO);

        verify(modelMapper).toEntity(modelDTO);
        verify(modelMapper).toDto(model);
        verify(modelRepository).save(model);
    }

    @DisplayName("Save Model - Model Already Exists - Throws ModelAlreadyExistsException")
    @Test
    void testSaveModel_whenModelAlreadyExists_throwsModelAlreadyExistsException() {

        //when
        Model model = mock(Model.class);
        when(modelRepository.findById(any(Integer.class))).thenReturn(Optional.of(model));

        //when
        Executable executable = () -> modelService.save(modelDTO);

        //then
        assertThrows(ModelAlreadyExistsException.class, executable, "Exception not match");
        verify(modelRepository).findById(any(Integer.class));
    }

    @DisplayName("Update Model")
    @Test
    void testUpdateModel_whenValidDetailsProvided_returnUpdatedDTO() throws ModelNotFoundException {

        //given
        Integer id = 1;
        Model model = Model.builder().id(id).brand(new Brand(1, "BMW", null)).build();
        when(modelRepository.findById(id)).thenReturn(Optional.of(model));
        when(modelMapper.toEntity(modelDTO)).thenReturn(model);
        when(modelMapper.toDto(model)).thenReturn(modelDTO);

        when(modelRepository.save(model)).thenReturn(model);

        //when
        ModelDTO updatedDTO = modelService.update(id, modelDTO);

        //then
        assertNotNull(updatedDTO, "Updated model should not be null");
        assertEquals(modelDTO, updatedDTO);

        verify(modelRepository).findById(any(Integer.class));
        verify(modelMapper).toDto(model);
        verify(modelRepository).save(model);
    }

    @DisplayName("Update Model Failed - Ivalid ID Provided")
    @Test
    void testUpdateModel_whenInvalidIdProvided_throwsModelNotFoundException() {

        //given
        Integer id = 1;
        when(modelRepository.findById(id)).thenReturn(Optional.empty());

        //when
        Executable executable = () -> modelService.update(id, modelDTO);

        assertThrows(ModelNotFoundException.class, executable, "Exception not match");
        verify(modelRepository).findById(id);
    }

    @DisplayName("Get All Models")
    @Test
    void testGetAllModels_whenListIsPopulated_thenReturnListModelDTO() {

        //given
        List<Model> models = List.of(mock(Model.class), mock(Model.class));
        when(modelRepository.findAll()).thenReturn(models);

        //when
        List<ModelDTO> modelDTOList = modelService.getAll();

        //then
        assertNotNull(modelDTOList);
        assertEquals(modelDTOList.size(), models.size());

        verify(modelRepository).findAll();
    }

    @DisplayName("Get Model By ID")
    @Test
    void testGetModelById_whenValidIdProvided_returnsModelDTO() throws ModelNotFoundException {

        //given
        Model model = mock(Model.class);
        Integer id = 1;
        when(modelRepository.findById(id)).thenReturn(Optional.of(model));
        when(modelMapper.toDto(model)).thenReturn(modelDTO);

        //when
        ModelDTO foundDTO = modelService.getById(id);

        //then
        assertNotNull(foundDTO);
        assertEquals(modelDTO, foundDTO);

        verify(modelRepository).findById(any(Integer.class));
        verify(modelMapper).toDto(model);
    }

    @DisplayName("Get Model By ID Failed - Invalid ID Provided")
    @Test
    void testGetModelById_whenInvalidIdProvided_throwsModelNotFoundException() {

        //given
        Integer id = 1;
        when(modelRepository.findById(id)).thenReturn(Optional.empty());

        //when
        Executable executable = () -> modelService.getById(id);

        //then
        assertThrows(ModelNotFoundException.class, executable, "Exception not match");
        verify(modelRepository).findById(id);
    }

    @DisplayName("Delete Model By ID")
    @Test
    void testDeleteModelById_whenValidIdProvided_thenCorrect() throws ModelNotFoundException {

        //given
        Integer id = 1;
        when(modelRepository.existsById(id)).thenReturn(true);
        doNothing().when(modelRepository).deleteById(id);

        //when
        modelService.delete(id);

        //then
        verify(modelRepository).existsById(id);
        verify(modelRepository).deleteById(id);
    }

    @DisplayName("Delete Model By ID Failed - Invalid ID Provided")
    @Test
    void testDeleteModelById_whenInvalidIdProvided_thenCorrect() {

        //given
        Integer id = 1;
        when(modelRepository.existsById(id)).thenReturn(false);

        //when
        Executable executable = () -> modelService.delete(id);

        //then
        assertThrows(ModelNotFoundException.class, executable, "Exception not match");
        verify(modelRepository).existsById(id);
    }
}