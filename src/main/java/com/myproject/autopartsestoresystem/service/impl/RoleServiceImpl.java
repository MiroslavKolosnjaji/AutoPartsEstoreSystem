package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.RoleDTO;
import com.myproject.autopartsestoresystem.exception.service.RoleNotFoundException;
import com.myproject.autopartsestoresystem.mapper.RoleMapper;
import com.myproject.autopartsestoresystem.model.Role;
import com.myproject.autopartsestoresystem.model.RoleName;
import com.myproject.autopartsestoresystem.repository.RoleRepository;
import com.myproject.autopartsestoresystem.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;


    @Override
    public RoleDTO getRoleByName(RoleName roleName) {
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
    public RoleDTO getRoleById(Long id) {
        return roleRepository.findById(id).map(roleMapper::roleToRoleDTO)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));
    }
}
