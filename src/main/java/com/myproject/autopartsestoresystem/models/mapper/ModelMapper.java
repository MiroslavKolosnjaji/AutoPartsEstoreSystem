package com.myproject.autopartsestoresystem.models.mapper;

import com.myproject.autopartsestoresystem.models.dto.ModelDTO;
import com.myproject.autopartsestoresystem.models.entity.Model;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface ModelMapper {

    ModelDTO modelToModelDTO(Model model);
    Model modelDtoToModel(ModelDTO modelDTO);
}
