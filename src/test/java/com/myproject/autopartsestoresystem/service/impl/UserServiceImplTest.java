package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.RoleDTO;
import com.myproject.autopartsestoresystem.dto.UserDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.service.RoleNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.UserNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.UsernameAlreadyExistsException;
import com.myproject.autopartsestoresystem.mapper.RoleMapper;
import com.myproject.autopartsestoresystem.mapper.UserMapper;
import com.myproject.autopartsestoresystem.model.Role;
import com.myproject.autopartsestoresystem.model.RoleName;
import com.myproject.autopartsestoresystem.model.User;
import com.myproject.autopartsestoresystem.repository.UserRepository;
import com.myproject.autopartsestoresystem.service.RoleService;
import com.myproject.autopartsestoresystem.service.UserAuthorityUpdateStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Miroslav Kološnjaji
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
                .username("test@example.com")
                .password("1233456432")
                .roles(Set.of())
                .enabled(true)
                .build();
    }

    @DisplayName("Save User")
    @Test
    void testSaveUser_whenValidDetailsProvided_returnsUserDTO() throws RoleNotFoundException, EntityAlreadyExistsException {

        //given
        User user = mock(User.class);
        RoleDTO roleDTO = mock(RoleDTO.class);

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(roleService.getRoleByName(RoleName.ROLE_USER)).thenReturn(roleDTO);
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("1233456432");
        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);
        when(userMapper.userDTOToUser(userDTO)).thenReturn(user);

        when(userRepository.save(user)).thenReturn(user);

        //when
        UserDTO savedDTO = userService.saveUser(userDTO);

        //then
        assertNotNull(savedDTO, "Saved UserDTO should not be null");
        assertEquals(userDTO, savedDTO, "Saved UserDTO should be equal to userDTO");

        verify(userRepository).findByUsername(anyString());
        verify(roleService).getRoleByName(RoleName.ROLE_USER);
        verify(passwordEncoder).encode(anyString());
        verify(userMapper).userToUserDTO(user);
        verify(userMapper).userDTOToUser(userDTO);
        verify(userRepository).save(user);
    }

    @DisplayName("Save User Failed - Username Already Exists")
    @Test
    void testSaveUser_whenUserAlreadyExists_throwsUsernameAlreadyExistsException() {

        //given
        User user = mock(User.class);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        //when
        Executable executable = () -> userService.saveUser(userDTO);

        //then
        assertThrows(UsernameAlreadyExistsException.class, executable, "Exception doesn't match. Expected UsernameAlreadyExistsException");
    }

    @DisplayName("Update User")
    @Test
    void testUpdateUser_whenValidDetailsProvided_returnsUserDTO() throws UserNotFoundException {

        //given
        User user = User.builder()
                .username("test@example.com")
                .password("1233456432")
                .roles(Set.of())
                .enabled(true)
                .build();

        when(userMapper.userToUserDTO(user)).thenReturn(userDTO);
        when(userMapper.userDTOToUser(userDTO)).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("1233456432");

        when(userRepository.save(user)).thenReturn(user);

        //when
        UserDTO updatedDTO = userService.update(anyLong(), userDTO);

        //then
        assertNotNull(updatedDTO, "Updated UserDTO should not be null");
        assertEquals(userDTO, updatedDTO, "Updated UserDTO should be equal to userDTO");

        verify(userRepository).findById(anyLong());
        verify(passwordEncoder).encode(anyString());
        verify(userMapper).userToUserDTO(user);
        verify(userMapper).userDTOToUser(userDTO);
        verify(userRepository).save(user);
    }

    @DisplayName("Update User Failed - Invalid ID Provided")
    @Test
    void testUpdateUser_whenInvalidIDProvided_throwsUserNotFoundException() {

        //given
        User user = mock(User.class);
        when(userMapper.userDTOToUser(userDTO)).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> userService.update(anyLong(), userDTO);

        //then
        assertThrows(UserNotFoundException.class, executable, "Exception doesn't match. Expected UserNotFoundException");
    }

    @DisplayName("Update User Authority - Grant Authority")
    @Test
    void testUpdateUserAuthority_whenValidDetailsProvided_thenGrantAuthorityToUser() throws RoleNotFoundException {

        //given
        User user = mock(User.class);
        RoleDTO roleDTO = mock(RoleDTO.class);
        Role role = mock(Role.class);
        UserAuthorityUpdateStatus updateStatus = UserAuthorityUpdateStatus.GRANT_AUTHORITY;
        RoleName roleName = RoleName.ROLE_ADMIN;

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleMapper.roleDTOToRole(roleDTO)).thenReturn(role);
        when(roleService.getRoleByName(roleName)).thenReturn(roleDTO);

        when(userRepository.save(user)).thenReturn(user);

        //when
        userService.updateUserAuthority(anyLong(), roleName, updateStatus);

        //then
        verify(userRepository).findById(anyLong());
        verify(roleService).getRoleByName(roleName);
        verify(roleMapper).roleDTOToRole(roleDTO);
        verify(userRepository).save(user);

    }

    @DisplayName("Update User Authority - Revoke Authority")
    @Test
    void testUpdateUserAuthority_whenValidDetailsProvided_thenRevokeAuthorityFromUser() throws RoleNotFoundException {

        //given
        User user = mock(User.class);
        RoleDTO roleDTO = mock(RoleDTO.class);
        Role role = mock(Role.class);
        UserAuthorityUpdateStatus updateStatus = UserAuthorityUpdateStatus.REVOKE_AUTHORITY;
        RoleName roleName = RoleName.ROLE_ADMIN;

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(roleMapper.roleDTOToRole(roleDTO)).thenReturn(role);
        when(roleService.getRoleByName(roleName)).thenReturn(roleDTO);

        when(userRepository.save(user)).thenReturn(user);

        //when
        userService.updateUserAuthority(anyLong(), roleName, updateStatus);

        //then
        verify(userRepository).findById(anyLong());
        verify(roleService).getRoleByName(roleName);
        verify(roleMapper).roleDTOToRole(roleDTO);
        verify(userRepository).save(user);
    }

    @DisplayName("Update User Authority Failed - Invalid ID Provided")
    @Test
    void testUpdateUserAuthority_whenIvalidIDProvided_throwsUsernameNotFoundException() {

        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> userService.updateUserAuthority(anyLong(), RoleName.ROLE_ADMIN, UserAuthorityUpdateStatus.REVOKE_AUTHORITY);

        //then
        assertThrows(UsernameNotFoundException.class, executable, "Exception doesn't match. Expected UsernameNotFoundException");
    }

    @DisplayName("Get All Users")
    @Test
    void testGetAllUsers_whenListIsPopulated_returnsListOfUserDTO() {

        //given
        List<User> users = List.of(mock(User.class), mock(User.class), mock(User.class));
        when(userRepository.findAll()).thenReturn(users);

        //when
        List<UserDTO> userDTOS = userService.getAll();

        //then
        assertNotNull(userDTOS, "UserDTO list should not be null");
        assertEquals(userDTOS.size(), users.size(), "UserDTO list should have the same size");

        verify(userRepository).findAll();
    }

    @DisplayName("Get User By ID")
    @Test
    void testGetUserByID_whenValidIDProvided_returnsUserDTO() throws UserNotFoundException {

        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mock(User.class)));
        when(userMapper.userToUserDTO(any(User.class))).thenReturn(userDTO);

        //when
        UserDTO foundDTO = userService.getById(anyLong());

        //then
        assertNotNull(foundDTO, "FoundDTO should not be null");
        assertEquals(userDTO, foundDTO, "FoundDTO should be equal to userDTO");

        verify(userRepository).findById(anyLong());
        verify(userMapper).userToUserDTO(any(User.class));
    }

    @DisplayName("Get User By ID Failed - Invalid ID Provided")
    @Test
    void testGetUserByID_whenInvalidIDProvided_throwsUserNotFoundException() {

        //given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Executable executable = () -> userService.getById(anyLong());

        //then
        assertThrows(UserNotFoundException.class, executable, "Exception doesn't match. Expected UserNotFoundException");
    }

    @DisplayName("Delete User By ID")
    @Test
    void testDeleteUserByID_whenValidIDProvided_thenCorrect() throws UserNotFoundException {

        //given
        when(userRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(userRepository).deleteById(anyLong());

        //when
        userService.delete(anyLong());

        //then
        verify(userRepository).existsById(anyLong());
        verify(userRepository).deleteById(anyLong());
    }

    @DisplayName("Delete User By ID Failed - Invalid ID Provided")
    @Test
    void testDeleteUserByID_whenInvalidIDProvided_throwsUserNotFoundException() {

        //given
        when(userRepository.existsById(anyLong())).thenReturn(false);

        //when
        Executable executable = () -> userService.delete(anyLong());

        //then
        assertThrows(UserNotFoundException.class, executable, "Exception doesn't match. Expected UserNotFoundException");
    }
}