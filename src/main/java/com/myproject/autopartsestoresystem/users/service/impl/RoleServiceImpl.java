package com.myproject.autopartsestoresystem.users.service.impl;

import com.myproject.autopartsestoresystem.users.dto.RoleDTO;
import com.myproject.autopartsestoresystem.users.exception.RoleNotFoundException;
import com.myproject.autopartsestoresystem.users.mapper.RoleMapper;
import com.myproject.autopartsestoresystem.users.entity.Role;
import com.myproject.autopartsestoresystem.users.entity.RoleName;
import com.myproject.autopartsestoresystem.users.repository.RoleRepository;
import com.myproject.autopartsestoresystem.users.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;


    @Override
    public RoleDTO getRoleByName(RoleName roleName) throws RoleNotFoundException {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        return roleMapper.roleToRoleDTO(role);
    }

    @Override
    public List<RoleDTO> getSelectedRoles(List<String> selectedRoles) {
        List<Role> roles = roleRepository.findByNameIn(selectedRoles);

        return roleMapper.rolesToRoleDTOs(roles);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::roleToRoleDTO)
                .toList();
    }

    @Override
    public RoleDTO getRoleById(Long id) throws RoleNotFoundException {
        return roleRepository.findById(id).map(roleMapper::roleToRoleDTO)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
    }
}
