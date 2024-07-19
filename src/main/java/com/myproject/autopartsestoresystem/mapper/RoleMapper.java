package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.RoleDTO;
import com.myproject.autopartsestoresystem.model.Role;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface RoleMapper {

    RoleDTO roleToRoleDTO(Role role);
    Role roleDTOToRole(RoleDTO roleDTO);

    @IterableMapping(elementTargetType = RoleDTO.class)
    List<RoleDTO> rolesToRoleDTOs(List<Role> roles);
}
