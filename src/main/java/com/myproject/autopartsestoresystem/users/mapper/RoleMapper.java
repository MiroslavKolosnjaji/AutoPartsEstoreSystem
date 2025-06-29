package com.myproject.autopartsestoresystem.users.mapper;

import com.myproject.autopartsestoresystem.users.dto.RoleDTO;
import com.myproject.autopartsestoresystem.users.entity.Role;
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
