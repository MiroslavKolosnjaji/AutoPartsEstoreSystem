package com.myproject.autopartsestoresystem.model;


import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Miroslav Kološnjaji
 */
class UserTest {


    @Test
    void testAllFields_isEqual_thenCorrect() {

        //given
        User user1 = User.builder()
                .id(1L)
                .username("test@example.com")
                .password("1233456432")
                .roles(Set.of())
                .enabled(true)
                .build();

        User user2 = User.builder()
                .id(1L)
                .username("test@example.com")
                .password("1233456432")
                .roles(Set.of())
                .enabled(true)
                .build();

        boolean isEqual = user1.equals(user2);
        boolean hashCodeEqual = user1.hashCode() == user2.hashCode();



        //when & then
        assertAll("User fields validation",
                () -> assertEquals(user1.getId(), user2.getId(), "User ID mismatch"),
                () -> assertEquals(user1.getUsername(), user2.getUsername(), "User username mismatch"),
                () -> assertEquals(user1.getPassword(), user2.getPassword(), "User password mismatch"),
                () -> assertEquals(user1.getRoles(), user2.getRoles(), "User roles mismatch"),
                () -> assertEquals(user1.isEnabled(), user2.isEnabled(), "User enabled mismatch"));

        assertTrue(isEqual, "User1 is not equal to User2");
        assertTrue(hashCodeEqual, "User1 hashCode is not equal to User2 hashCode");

    }
}