package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.customer.ModelDTO;
import com.myproject.autopartsestoresystem.model.Model;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface ModelMapper {

    ModelDTO modelToModelDTO(Model model);
    Model modelDtoToModel(ModelDTO modelDTO);
}
