package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.PartGroupDTO;
import com.myproject.autopartsestoresystem.model.PartGroup;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface PartGroupMapper {

    PartGroupDTO partGroupToPartGroupDTO(PartGroup partGroup);
    PartGroup partGroupDTOToPartGroup(PartGroupDTO partGroupDTO);
}
