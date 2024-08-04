package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.RoleDTO;
import com.myproject.autopartsestoresystem.exception.service.RoleNotFoundException;
import com.myproject.autopartsestoresystem.model.RoleName;

import java.util.List;
import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface RoleService {

    RoleDTO getRoleByName(RoleName roleName) throws RoleNotFoundException;
    List<RoleDTO> getSelectedRoles(List<String> selectedRoles);
    List<RoleDTO> getAllRoles();
    RoleDTO getRoleById(Long id) throws RoleNotFoundException;
}
