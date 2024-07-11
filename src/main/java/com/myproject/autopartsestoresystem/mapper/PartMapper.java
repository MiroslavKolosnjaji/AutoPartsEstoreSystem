package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.PartDTO;
import com.myproject.autopartsestoresystem.model.Part;
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
