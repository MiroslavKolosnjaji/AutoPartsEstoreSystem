package com.myproject.autopartsestoresystem.users.service.impl;

import com.myproject.autopartsestoresystem.users.dto.RoleDTO;
import com.myproject.autopartsestoresystem.users.exception.RoleNotFoundException;
import com.myproject.autopartsestoresystem.users.mapper.RoleMapper;
import com.myproject.autopartsestoresystem.users.entity.Role;
import com.myproject.autopartsestoresystem.users.entity.RoleName;
import com.myproject.autopartsestoresystem.users.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    private RoleDTO roleDTO;


    @BeforeEach
    void setUp() {
        roleDTO = RoleDTO.builder()
                .name(RoleName.ROLE_ADMIN)
                .build();
    }

    @DisplayName("Get Role By Name")
    @Test
    void testGetRoleByName_whenValidDetailsProvided_returnsRoleDTO() throws RoleNotFoundException {

        //given
        Role role = mock(Role.class);
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(role));
        when(roleMapper.roleToRoleDTO(role)).thenReturn(roleDTO);

        //when
        RoleDTO foundDTO = roleService.getRoleByName(RoleName.ROLE_ADMIN);

        //then
        assertNotNull(foundDTO, "FoundDTO should not be null");
        assertEquals(roleDTO, foundDTO, "FoundDTO should be the same");

        verify(roleRepository).findByName(RoleName.ROLE_ADMIN);
        verify(roleMapper).roleToRoleDTO(role);
    }

    @DisplayName("Get Role By Name Failed - Role Doesn't Exists")
    @Test
    void testGetRoleByName_whenRoleDoesntExistsInBase_throwsRoleNotFoundException() {

        //given
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.empty());

        //when
        Executable executable = () -> roleService.getRoleByName(RoleName.ROLE_ADMIN);

        //then
        assertThrows(RoleNotFoundException.class, executable, "Exception mismatch. RoleNotFoundException expected");
    }

    @DisplayName("Get Selected Roles")
    @Test
    void testGetSelectedRoles_whenValidInputProvided_returnsListOfRoleDTO() {

        //given
        List<Role> roles = List.of(mock(Role.class));
        List<RoleDTO> roleDTOs = List.of(roleDTO);
        when(roleRepository.findByNameIn(List.of("ADMIN"))).thenReturn(roles);
        when(roleMapper.rolesToRoleDTOs(roles)).thenReturn(roleDTOs);

        //when
        List<RoleDTO> foundRoleDTOs = roleService.getSelectedRoles(List.of("ADMIN"));

        //then
        assertNotNull(foundRoleDTOs, "FoundRoleDTOs should not be null");
        assertEquals(roleDTOs, foundRoleDTOs, "FoundRoleDTOs should be the same");
    }

    @Test
    void testGetAllRoles_whenListIsPopulated_returnsListOfRoleDTO() {

        //given
        List<Role> roles = List.of(mock(Role.class));
        when(roleRepository.findAll()).thenReturn(roles);

        //when
        List<RoleDTO> roleDTOs = roleService.getAllRoles();

        //then
        assertNotNull(roleDTOs, "FoundRoleDTOs should not be null");
        assertEquals(1, roleDTOs.size(), "FoundRoleDTOs should have one role");
    }

    @DisplayName("Get Role By ID")
    @Test
    void testGetRoleByID_whenValidIdProvided_returnsRoleDTO() throws RoleNotFoundException {

        //given
        Role role = mock(Role.class);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleMapper.roleToRoleDTO(role)).thenReturn(roleDTO);

        //when
        RoleDTO foundDTO = roleService.getRoleById(1L);

        //then
        assertNotNull(foundDTO, "FoundDTO should not be null");
        assertEquals(roleDTO, foundDTO, "FoundDTO should be the same");
    }

    @DisplayName("Get Role By ID Failed - Invalid ID Provided")
    @Test
    void testGetRoleByID_whenInvalidIDProvided_throwsRoleNotFoundException() {

        //given
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        Executable executable = () -> roleService.getRoleById(1L);

        //then
        assertThrows(RoleNotFoundException.class, executable, "Exception mismatch. RoleNotFoundException expected");
    }
}