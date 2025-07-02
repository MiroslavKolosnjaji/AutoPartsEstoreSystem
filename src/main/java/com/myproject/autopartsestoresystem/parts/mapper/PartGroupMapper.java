package com.myproject.autopartsestoresystem.parts.mapper;

import com.myproject.autopartsestoresystem.parts.dto.PartGroupDTO;
import com.myproject.autopartsestoresystem.parts.entity.PartGroup;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface PartGroupMapper {

    PartGroupDTO partGroupToPartGroupDTO(PartGroup partGroup);
    PartGroup partGroupDTOToPartGroup(PartGroupDTO partGroupDTO);
}
