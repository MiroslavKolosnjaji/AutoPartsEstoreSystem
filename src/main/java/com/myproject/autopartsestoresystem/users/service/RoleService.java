package com.myproject.autopartsestoresystem.users.service;

import com.myproject.autopartsestoresystem.users.dto.RoleDTO;
import com.myproject.autopartsestoresystem.users.exception.RoleNotFoundException;
import com.myproject.autopartsestoresystem.users.entity.RoleName;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
public interface RoleService {

    RoleDTO getRoleByName(RoleName roleName) throws RoleNotFoundException;
    List<RoleDTO> getSelectedRoles(List<String> selectedRoles);
    List<RoleDTO> getAllRoles();
    RoleDTO getRoleById(Long id) throws RoleNotFoundException;
}
