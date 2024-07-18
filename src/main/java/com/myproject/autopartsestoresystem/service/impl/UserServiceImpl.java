package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.UserDTO;
import com.myproject.autopartsestoresystem.exception.service.UserNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.UsernameAlreadyExistsException;
import com.myproject.autopartsestoresystem.mapper.UserMapper;
import com.myproject.autopartsestoresystem.model.User;
import com.myproject.autopartsestoresystem.repository.UserRepository;
import com.myproject.autopartsestoresystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public UserDTO save(UserDTO userDTO) {

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent())
            throw new UsernameAlreadyExistsException("Username already exists");

        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        User savedUser = userRepository.save(userMapper.userDTOToUser(userDTO));

        return userMapper.userToUserDTO(savedUser);
    }

    @Override
    public UserDTO update(Long id, UserDTO userDTO) {

        User user = userMapper.userDTOToUser(userDTO);

        User updatedUser = userRepository.findById(id)
                .map(u -> {

                    u.setUsername(user.getUsername());
                    u.setPassword(passwordEncoder.encode(user.getPassword()));
                    u.setEnabled(user.isEnabled());
                    u.setRoles(user.getRoles());

                    return userRepository.save(u);

                }).orElseThrow(() -> new UserNotFoundException("User not found"));


        return userMapper.userToUserDTO(updatedUser);
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(userMapper::userToUserDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO getById(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        return userMapper.userToUserDTO(user);
    }

    @Override
    public void delete(Long id) {

        if (!userRepository.existsById(id))
            throw new UserNotFoundException("User not found");

        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
