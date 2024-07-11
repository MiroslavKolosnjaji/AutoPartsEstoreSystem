package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.ModelDTO;
import com.myproject.autopartsestoresystem.exception.service.ModelAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.service.ModelNotFoundException;
import com.myproject.autopartsestoresystem.mapper.ModelMapper;
import com.myproject.autopartsestoresystem.model.Brand;
import com.myproject.autopartsestoresystem.model.Model;
import com.myproject.autopartsestoresystem.model.ModelId;
import com.myproject.autopartsestoresystem.repository.ModelRepository;
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
                .id(new ModelId(1L, "330"))
                .brand(new Brand(1L, "BMW", null))
                .build();
    }

    @DisplayName("Save Model")
    @Test
    void testSaveModel_whenValidDetailsProvided_returnsModelDTO() {

        //given
        Model model = mock(Model.class);
        when(modelMapper.modelDtoToModel(modelDTO)).thenReturn(model);
        when(modelMapper.modelToModelDTO(model)).thenReturn(modelDTO);

        when(modelRepository.save(model)).thenReturn(model);

        //when
        ModelDTO savedDTO = modelService.save(modelDTO);

        //then
        assertNotNull(savedDTO);
        assertEquals(modelDTO, savedDTO);

        verify(modelMapper).modelDtoToModel(modelDTO);
        verify(modelMapper).modelToModelDTO(model);
        verify(modelRepository).save(model);
    }

    @DisplayName("Save Model - Model Already Exists - Throws ModelAlreadyExistsException")
    @Test
    void testSaveModel_whenModelAlreadyExists_throwsModelAlreadyExistsException() {

        //when
        Model model = mock(Model.class);
        when(modelRepository.findById(any(ModelId.class))).thenReturn(Optional.of(model));

        //when
        Executable executable = () -> modelService.save(modelDTO);

        //then
        assertThrows(ModelAlreadyExistsException.class, executable, "Exception not match");
        verify(modelRepository).findById(any(ModelId.class));
    }

    @DisplayName("Update Model")
    @Test
    void testUpdateModel_whenValidDetailsProvided_returnUpdatedDTO() {

        //given
        ModelId modelId = new ModelId(1L, "335");
        Model model = Model.builder().id(modelId).brand(new Brand(1L, "BMW", null)).build();
        when(modelRepository.findById(modelId)).thenReturn(Optional.of(model));
        when(modelMapper.modelToModelDTO(model)).thenReturn(modelDTO);

        when(modelRepository.save(model)).thenReturn(model);

        //when
        ModelDTO updatedDTO = modelService.update(modelId, modelDTO);

        //then
        assertNotNull(updatedDTO, "Updated model should not be null");
        assertEquals(modelDTO, updatedDTO);

        verify(modelRepository).findById(any(ModelId.class));
        verify(modelMapper).modelToModelDTO(model);
        verify(modelRepository).save(model);
    }

    @DisplayName("Update Model Failed - Ivalid ID Provided")
    @Test
    void testUpdateModel_whenInvalidIdProvided_throwsModelNotFoundException() {

        //given
        ModelId modelId = new ModelId(1L, null);
        when(modelRepository.findById(modelId)).thenReturn(Optional.empty());

        //when
        Executable executable = () -> modelService.update(modelId, modelDTO);

        assertThrows(ModelNotFoundException.class, executable, "Exception not match");
        verify(modelRepository).findById(modelId);
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
    void testGetModelById_whenValidIdProvided_returnsModelDTO() {

        //given
        Model model = mock(Model.class);
        when(modelRepository.findById(new ModelId(1L, "330"))).thenReturn(Optional.of(model));
        when(modelMapper.modelToModelDTO(model)).thenReturn(modelDTO);

        //when
        ModelDTO foundDTO = modelService.getById(new ModelId(1L, "330"));

        //then
        assertNotNull(foundDTO);
        assertEquals(modelDTO, foundDTO);

        verify(modelRepository).findById(any(ModelId.class));
        verify(modelMapper).modelToModelDTO(model);
    }

    @DisplayName("Get Model By ID Failed - Invalid ID Provided")
    @Test
    void testGetModelById_whenInvalidIdProvided_throwsModelNotFoundException() {

        //given
        when(modelRepository.findById(new ModelId(1L, "330"))).thenReturn(Optional.empty());

        //when
        Executable executable = () -> modelService.getById(new ModelId(1L, "330"));

        //then
        assertThrows(ModelNotFoundException.class, executable, "Exception not match");
    }

    @DisplayName("Delete Model By ID")
    @Test
    void testDeleteModelById_whenValidIdProvided_thenCorrect() {

        //given
        ModelId modelId = new ModelId(1L, "330");
        when(modelRepository.existsById(modelId)).thenReturn(true);
        doNothing().when(modelRepository).deleteById(modelId);

        //when
        modelService.delete(modelId);

        //then
        verify(modelRepository).existsById(modelId);
        verify(modelRepository).deleteById(modelId);
    }

    @DisplayName("Delete Model By ID Failed - Invalid ID Provided")
    @Test
    void testDeleteModelById_whenInvalidIdProvided_thenCorrect() {

        //given
        when(modelRepository.existsById(new ModelId(1L, "330"))).thenReturn(false);

        //when
        Executable executable = () -> modelService.delete(new ModelId(1L, "330"));

        //then
        assertThrows(ModelNotFoundException.class, executable, "Exception not match");
    }
}