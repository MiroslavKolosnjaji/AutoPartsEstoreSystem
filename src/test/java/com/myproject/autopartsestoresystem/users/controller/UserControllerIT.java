package com.myproject.autopartsestoresystem.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.controller.BaseIT;
import com.myproject.autopartsestoresystem.users.dto.UserDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Miroslav Kološnjaji
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIT extends BaseIT {

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
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreateUser_whenValidDetailsProvided_return201StatusCode(String user, String password) throws Exception {

        mockMvc.perform(post(UserController.USER_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", UserController.USER_URI + "/" + 4))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(userDTO.getUsername()));
    }

    @DisplayName("Create User Failed - Invalid Details Provided")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testCreateUser_whenInvalidDetailsProvided_returns400StatusCode(String user, String password) throws Exception {

        userDTO.setUsername("123123");

        mockMvc.perform(post(UserController.USER_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Create User Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testCreateUser_withUserRole_return403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(post(UserController.USER_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Create User Failed - No Role - Returns Code 401")
    @Test
    void testCreateUser_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(post(UserController.USER_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Update User")
    @Order(2)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testUpdateUser_whenValidDetailsProvided_returns204StatusCode(String user, String password) throws Exception {

        userDTO.setId(1L);
        userDTO.setPassword("asdfzcxv");

        mockMvc.perform(put(UserController.USER_URI_WITH_ID, 4)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Update User Failed - Invalid Details Provided")
    @Order(3)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testUpdateUser_whenInvalidDetailsProvided_returns400StatusCode(String user, String password) throws Exception {

        userDTO.setUsername("1443");

        mockMvc.perform(put(UserController.USER_URI_WITH_ID, 4)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Update User Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testUpdateUser_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        userDTO.setId(1L);
        userDTO.setPassword("asdfzcxv");

        mockMvc.perform(put(UserController.USER_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Get All Users")
    @Order(4)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testGetAllUsers_whenListIsPopulated_returns200StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(UserController.USER_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(4));
    }

    @DisplayName("Get All Users Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testGetAllUsers_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(UserController.USER_URI)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Get All Users Failed - No Role - Returns Code 401")
    @Test
    void testGetAllUsers_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(UserController.USER_URI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Get User By ID")
    @Order(5)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testGetUserById_whenValidIdProvided_returns200StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(UserController.USER_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Content-type", "application/json"))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.roles.length()").value(1));
    }

    @DisplayName("Get User By ID Failed - Invalid ID Provided")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testGetUserById_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(UserController.USER_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Get User By ID Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testGetUserById_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(get(UserController.USER_URI_WITH_ID, 1)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Get User By ID Failed - No Role - Returns Code 401")
    @Test
    void testGetUserByID_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(get(UserController.USER_URI_WITH_ID, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Delete User")
    @Order(99)
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_USER)
    void testDeleteUserById_whenValidIdProvided_returns204StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(UserController.USER_URI_WITH_ID, 4)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Delete User Failed - Invalid ID Provided - Returns Code 404")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_ADMIN_AND_MODERATOR_USERS)
    void testDeleteUserById_whenInvalidIdProvided_returns404StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(UserController.USER_URI_WITH_ID, 99)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Delete User Failed - User Role - Returns Code 403")
    @ParameterizedTest(name = IDX_WITH_ARGS)
    @MethodSource(GET_USER)
    void testDeleteUserById_withUserRole_returns403StatusCode(String user, String password) throws Exception {

        mockMvc.perform(delete(UserController.USER_URI_WITH_ID, 4)
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DisplayName("Delete User Failed - No Role - Returns Code 401")
    @Test
    void testDeleteUser_withoutAnyRole_returns401StatusCode() throws Exception {

        mockMvc.perform(delete(UserController.USER_URI_WITH_ID, 4)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}