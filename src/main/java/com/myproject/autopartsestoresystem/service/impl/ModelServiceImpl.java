package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.ModelDTO;
import com.myproject.autopartsestoresystem.exception.service.ModelAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.service.ModelNotFoundException;
import com.myproject.autopartsestoresystem.mapper.ModelMapper;
import com.myproject.autopartsestoresystem.model.Model;
import com.myproject.autopartsestoresystem.model.ModelId;
import com.myproject.autopartsestoresystem.repository.ModelRepository;
import com.myproject.autopartsestoresystem.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final ModelMapper modelMapper;

    @Override
    public ModelDTO save(ModelDTO modelDTO) {

        if (modelRepository.findById(modelDTO.getId()).isPresent())
            throw new ModelAlreadyExistsException("Model already exists");

        Model saved = modelRepository.save(modelMapper.modelDtoToModel(modelDTO));

        return modelMapper.modelToModelDTO(saved);
    }


    @Override
    public ModelDTO update(ModelId id, ModelDTO modelDTO) {

        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Model not found"));

        model.getId().setName(modelDTO.getId().getName());

        modelRepository.save(model);

        return modelMapper.modelToModelDTO(model);
    }

    @Override
    public List<ModelDTO> getAll() {
        return modelRepository.findAll().stream().map(modelMapper::modelToModelDTO).collect(Collectors.toList());
    }

    @Override
    public ModelDTO getById(ModelId id) {
        Model model = modelRepository.findById(id).orElseThrow(() -> new ModelNotFoundException("Model not found"));
        return modelMapper.modelToModelDTO(model);
    }

    @Override
    @Transactional
    public void delete(ModelId id) {

        if(modelRepository.findById(id).isEmpty())
            throw new ModelNotFoundException("Model not found");

        modelRepository.deleteById(id);
    }
}
