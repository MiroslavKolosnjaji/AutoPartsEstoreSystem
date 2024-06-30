package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.PartDTO;
import com.myproject.autopartsestoresystem.model.Part;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface PartMapper {

    PartDTO partToPartDTO(Part part);
    Part partDTOToPart(PartDTO partDTO);
}
