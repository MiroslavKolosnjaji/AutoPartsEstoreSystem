package com.myproject.autopartsestoresystem.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class RoleTest {

    @Test
    void testAllFields_isEqual_thenCorrect() {

        //given
        Role role1 = Role.builder()
                .id(1L)
                .name(RoleName.ROLE_ADMIN)
                .build();

        Role role2 = Role.builder()
                .id(1L)
                .name(RoleName.ROLE_ADMIN)
                .build();

        boolean equals = role1.equals(role2);
        boolean hashCode = role1.hashCode() == role2.hashCode();

        assertAll("Role fields validation",
                () -> assertEquals(role1.getId(), role2.getId(), "Role ID mismatch"),
                () -> assertEquals(role1.getName(), role2.getName(), "Role name mismatch"));

        assertTrue(equals, "Role should be equal");
        assertTrue(hashCode, "Role hashCode mismatch");
    }
}