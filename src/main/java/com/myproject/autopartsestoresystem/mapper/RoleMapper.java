package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.RoleDTO;
import com.myproject.autopartsestoresystem.model.Role;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface RoleMapper {

    RoleDTO roleToRoleDTO(Role role);
    Role roleDTOToRole(RoleDTO roleDTO);
}
