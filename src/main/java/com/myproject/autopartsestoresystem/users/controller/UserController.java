package com.myproject.autopartsestoresystem.users.controller;

import com.myproject.autopartsestoresystem.users.dto.UserDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.security.permission.user.*;
import com.myproject.autopartsestoresystem.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    public static final String USER_URI = "/api/user";
    public static final String USER_ID = "/{userId}";
    public static final String USER_URI_WITH_ID = USER_URI + USER_ID;

    private final UserService userService;

    @UserCreatePermission
    @PostMapping
    public ResponseEntity<UserDTO> saveUser(@Validated @RequestBody UserDTO userDTO) throws EntityAlreadyExistsException, EntityNotFoundException {

        UserDTO saved = userService.saveUser(userDTO);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.LOCATION, USER_URI + "/" + saved.getId());

        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @UserUpdatePermission
    @PutMapping(USER_ID)
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") Long userId, @Validated @RequestBody UserDTO userDTO) throws EntityNotFoundException {

        UserDTO updated = userService.update(userId, userDTO);
        return new ResponseEntity<>(updated, HttpStatus.NO_CONTENT);
    }

    @UserReadPermission
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<UserDTO> users = userService.getAll();

        if (users.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @UserReadPermission
    @GetMapping(USER_ID)
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") Long userId) throws EntityNotFoundException {

        UserDTO userDTO = userService.getById(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @UserDeletePermission
    @DeleteMapping(USER_ID)
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) throws EntityNotFoundException {

        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
