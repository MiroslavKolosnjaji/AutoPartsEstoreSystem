package com.myproject.autopartsestoresystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.dto.UpdateUserAuthorityRequest;
import com.myproject.autopartsestoresystem.dto.UserDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        userDTO = UserDTO.builder()
                .username("test@example.com")
                .password("1233456432")
                .roles(Set.of())
                .enabled(true)
                .build();
    }

    @DisplayName("Create User")
    @Order(1)
    @Test
    void testCreateUser_whenValidDetailsProvided_return201StatusCode() throws Exception {

        mockMvc.perform(post(UserController.USER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", UserController.USER_URI + "/" + 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(userDTO.getUsername()));
    }

    @DisplayName("Create User Failed - Invalid Details Provided")
    @Test
    void testCreateUser_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        userDTO.setUsername("123123");

        mockMvc.perform(post(UserController.USER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Update User")
    @Order(2)
    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateUser_whenValidDetailsProvided_returns204StatusCode() throws Exception {

        userDTO.setId(1L);
        userDTO.setPassword("asdfzcxv");

        mockMvc.perform(put(UserController.USER_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Update User Failed - Invalid Details Provided")
    @Test
    void testUpdateUser_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        userDTO.setUsername("1443");

        mockMvc.perform(put(UserController.USER_URI_WITH_ID,  1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Update User Authority")
    @Test
    @Order(3)
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    void testUpdateUserAuthority_whenValidInputProvided_returns200StatusCode() throws Exception {

        UpdateUserAuthorityRequest updateUserAuthorityRequest = new UpdateUserAuthorityRequest();
        updateUserAuthorityRequest.setAuthority("ROLE_ADMIN");
        updateUserAuthorityRequest.setUpdateStatus("GRANT_AUTHORITY");

        mockMvc.perform(patch(UserController.USER_URI_WITH_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserAuthorityRequest)))
                .andExpect(status().isOk());
    }

    @DisplayName("Get All Users")
    @Order(4)
    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    void testGetAllUsers_whenListIsPopulated_returns200StatusCode() throws Exception {

        mockMvc.perform(get(UserController.USER_URI)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @DisplayName("Get User By ID")
    @Order(5)
    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    void testGetUserById_whenValidIdProvided_returns200StatusCode() throws Exception {

        mockMvc.perform(get(UserController.USER_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.username").value(userDTO.getUsername()))
                .andExpect(jsonPath("$.roles.length()").value(2));
    }

    @DisplayName("Get User By ID Failed - Invalid ID Provided")
    @Test
    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    void testGetUserById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        mockMvc.perform(get(UserController.USER_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete User")
    @Order(6)
    @Test
    void testDeleteUserById_whenValidIdProvided_returns204StatusCode() throws Exception {

        mockMvc.perform(delete(UserController.USER_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUserById_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        mockMvc.perform(delete(UserController.USER_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}