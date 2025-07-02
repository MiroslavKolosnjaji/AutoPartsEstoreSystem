package com.myproject.autopartsestoresystem.parts.mapper;

import com.myproject.autopartsestoresystem.parts.dto.PartDTO;
import com.myproject.autopartsestoresystem.parts.entity.Part;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface PartMapper {

    PartDTO partToPartDTO(Part part);
    Part partDTOToPart(PartDTO partDTO);

    @IterableMapping(elementTargetType = PartDTO.class)
    List<PartDTO> partsToPartDTOs(List<Part> parts);
}
