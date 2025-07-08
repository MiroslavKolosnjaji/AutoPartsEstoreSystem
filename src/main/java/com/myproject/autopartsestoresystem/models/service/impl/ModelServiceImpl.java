package com.myproject.autopartsestoresystem.models.service.impl;

import com.myproject.autopartsestoresystem.models.dto.ModelDTO;
import com.myproject.autopartsestoresystem.models.exception.ModelAlreadyExistsException;
import com.myproject.autopartsestoresystem.models.exception.ModelNotFoundException;
import com.myproject.autopartsestoresystem.models.mapper.ModelMapper;
import com.myproject.autopartsestoresystem.models.entity.Model;
import com.myproject.autopartsestoresystem.models.repository.ModelRepository;
import com.myproject.autopartsestoresystem.models.service.ModelService;
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
    public ModelDTO save(ModelDTO modelDTO) throws ModelAlreadyExistsException {

        if (modelRepository.findById(modelDTO.getId()).isPresent())
            throw new ModelAlreadyExistsException("Model already exists");

        Model saved = modelRepository.save(modelMapper.toEntity(modelDTO));

        return modelMapper.toDto(saved);
    }


    @Override
    public ModelDTO update(Integer id, ModelDTO modelDTO) throws ModelNotFoundException {

        modelRepository.findById(id)
                .orElseThrow(() -> new ModelNotFoundException("Model not found"));

        Model updatedModel = modelRepository.save(modelMapper.toEntity(modelDTO));

        return modelMapper.toDto(updatedModel);
    }

    @Override
    public List<ModelDTO> getAll() {
        return modelRepository.findAll().stream().map(modelMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ModelDTO getById(Integer id) throws ModelNotFoundException {
        Model model = modelRepository.findById(id).orElseThrow(() -> new ModelNotFoundException("Model not found"));
        return modelMapper.toDto(model);
    }

    @Override
    @Transactional
    public void delete(Integer id) throws ModelNotFoundException {

        if (!modelRepository.existsById(id))
            throw new ModelNotFoundException("Model not found");

        modelRepository.deleteById(id);
    }
}
