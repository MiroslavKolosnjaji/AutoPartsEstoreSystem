package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.UpdateUserAuthorityRequest;
import com.myproject.autopartsestoresystem.dto.UserDTO;
import com.myproject.autopartsestoresystem.model.RoleName;
import com.myproject.autopartsestoresystem.service.UserAuthorityUpdateStatus;
import com.myproject.autopartsestoresystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping
    public ResponseEntity<UserDTO> saveUser(@Validated @RequestBody UserDTO userDTO) {

        UserDTO saved = userService.save(userDTO);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.LOCATION, USER_URI + "/" + saved.getId());

        return new ResponseEntity<>(saved, responseHeaders, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'USER')")
    @PutMapping(USER_ID)
    public ResponseEntity<UserDTO> updateUser(@PathVariable("userId") Long userId, @Validated @RequestBody UserDTO userDTO) {

        UserDTO updated = userService.update(userId, userDTO);
        return new ResponseEntity<>(updated, HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PatchMapping(USER_ID)
    public ResponseEntity<UserDTO> updateUserAuthority(@PathVariable("userId") Long userId, @Valid @RequestBody UpdateUserAuthorityRequest request) {

        userService.updateUserAuthority(userId, RoleName.valueOf(request.getAuthority()), UserAuthorityUpdateStatus.valueOf(request.getUpdateStatus()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<UserDTO> users = userService.getAll();

        if (users.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping(USER_ID)
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") Long userId) {

        UserDTO userDTO = userService.getById(userId);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @DeleteMapping(USER_ID)
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {

        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
