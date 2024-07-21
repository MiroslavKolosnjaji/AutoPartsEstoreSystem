package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.RoleDTO;
import com.myproject.autopartsestoresystem.model.RoleName;
import com.myproject.autopartsestoresystem.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class RoleServiceImplTestIT {

    @Autowired
    private RoleService roleService;
    private RoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        roleDTO = RoleDTO.builder()
                .name(RoleName.ROLE_ADMIN)
                .build();
    }

    @Test
    void testGetRoleByName_whenValidInputProvided_returnsRoleDTO() {

        //given

        //when
        RoleDTO foundRoleDTO = roleService.getRoleByName(RoleName.ROLE_ADMIN);

        //then
        assertNotNull(foundRoleDTO);
        assertNotNull(foundRoleDTO.getId());
        assertEquals(RoleName.ROLE_ADMIN, foundRoleDTO.getName());
    }

    @Test
    void testGetSelectedRoles_whenValidInputProvided_returnsListOfRoleDTO() {

        //given

        //when
        List<RoleDTO> foundRoleDTOList = roleService.getSelectedRoles(List.of(RoleName.ROLE_USER.name(), RoleName.ROLE_ADMIN.name()));

        //then
        assertNotNull(foundRoleDTOList);
        assertEquals(2, foundRoleDTOList.size());
    }

    @Test
    void testGetAllRoles_whenListIsPopulated_returnsListOfRoleDTO() {
        //given

        //when
        List<RoleDTO> foundRoleDTOList = roleService.getAllRoles();

        //then
        assertNotNull(foundRoleDTOList);
        assertEquals(4, foundRoleDTOList.size());
    }

    @Test
    void testGetRoleById_whenValidIDProvided_returnsRoleDTO() {

        //given

        //when
        RoleDTO foundRoleDTO = roleService.getRoleById(1L);

        //then
        assertNotNull(foundRoleDTO);
        assertNotNull(foundRoleDTO.getId());
        assertEquals(RoleName.ROLE_ADMIN, foundRoleDTO.getName());
    }
}