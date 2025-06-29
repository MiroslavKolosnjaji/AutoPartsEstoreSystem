package com.myproject.autopartsestoresystem.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.autopartsestoresystem.users.dto.UserDTO;
import com.myproject.autopartsestoresystem.users.exception.UserNotFoundException;
import com.myproject.autopartsestoresystem.users.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @author Miroslav Kološnjaji
 */
@WebMvcTest(controllers = UserController.class)
@MockBean(UserService.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

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
    @Test
    void testCreateUser_whenValidDetailsProvided_returns200StatusCode() throws Exception {

        //given
        when(userService.saveUser(userDTO)).thenReturn(userDTO);

        RequestBuilder requestBuilder = post(UserController.USER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        UserDTO savedUserDTO = objectMapper.readValue(response, UserDTO.class);

        //then
        assertNotNull(savedUserDTO, "Saved UserDTO should not be null");
        assertEquals(userDTO, savedUserDTO, "Saved UserDTO did not match");
        verify(userService).saveUser(userDTO);
    }

    @DisplayName("Create User Failed - Invalid Details Provided")
    @Test
    void testCreateUser_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        //given
        userDTO.setPassword(null);

        RequestBuilder requestBuilder = post(UserController.USER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Update User")
    @Test
    void testUpdateUser_whenValidDetailsProvided_returns204StatusCode() throws Exception {

        //given
        userDTO.setUsername("admin@example.com");
        when(userService.update(anyLong(), any(UserDTO.class))).thenReturn(userDTO);

        RequestBuilder requestBuilder = put(UserController.USER_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(userService).update(anyLong(), any(UserDTO.class));
    }

    @DisplayName("Update User Failed - Invalid Details Provided")
    @Test
    void testUpdateUser_whenInvalidDetailsProvided_returns400StatusCode() throws Exception {

        //given
        userDTO.setPassword(null);

        RequestBuilder requestBuilder = put(UserController.USER_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 400 expected");
    }

    @DisplayName("Get All Users")
    @Test
    void testGetAllUsers_whenListIsPopulated_returns200StatusCode() throws Exception {

        //given
        List<UserDTO> userDTOS = List.of(mock(UserDTO.class), mock(UserDTO.class));
        when(userService.getAll()).thenReturn(userDTOS);

        RequestBuilder requestBuilder = get(UserController.USER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTOS));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        int listSize = objectMapper.readValue(result.getResponse().getContentAsString(), List.class).size();

        //then
        assertEquals(2, listSize, "List size should be 2");
        verify(userService).getAll();
    }

    @DisplayName("Get All Users - List is Empty")
    @Test
    void testGetAllVehicles_whenListIsEmpty_returns204StatusCode() throws Exception {

        //given
        List<UserDTO> userDTOS = List.of();
        when(userService.getAll()).thenReturn(userDTOS);

        RequestBuilder requestBuilder = get(UserController.USER_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTOS));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(userService).getAll();
    }

    @DisplayName("Get User By ID")
    @Test
    void testGetUserById_whenValidIdProvided_returns200StatusCode() throws Exception {

        //given
        when(userService.getById(anyLong())).thenReturn(userDTO);

        RequestBuilder requestBuilder = get(UserController.USER_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String response = result.getResponse().getContentAsString();
        UserDTO foundDTO = objectMapper.readValue(response, UserDTO.class);

        //then
        assertNotNull(foundDTO, "FoundDTO should not be null");
        assertEquals(userDTO, foundDTO, "FoundDTO should equal userDTO");
        verify(userService).getById(anyLong());
    }

    @DisplayName("Get User By ID Failed - Invalid ID Provided")
    @Test
    void testGetUserByID_whenInvalidIdProvided_returns404StatusCode() throws Exception {

        //given
        when(userService.getById(anyLong())).thenThrow(UserNotFoundException.class);

        RequestBuilder requestBuilder = get(UserController.USER_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();


        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
        verify(userService).getById(anyLong());
    }

    @Test
    void testDeleteUser_whenValidIdProvided_returns204StatusCode() throws Exception {

        //given
        doNothing().when(userService).delete(anyLong());

        RequestBuilder requestBuilder = delete(UserController.USER_URI_WITH_ID, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO));

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 204 expected");
        verify(userService).delete(anyLong());
    }

    @DisplayName("Delete User By ID Failed - Invalid ID Provided")
    @Test
    void testDeleteUser_whenInvalidIDProvided_returns404StatusCode() throws Exception {

        //given
        doThrow(UserNotFoundException.class).when(userService).delete(anyLong());

        RequestBuilder requestBuilder = delete(UserController.USER_URI_WITH_ID, 99)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        //when
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //then
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "Incorrect status code returned, status code 404 expected");
    }
}
