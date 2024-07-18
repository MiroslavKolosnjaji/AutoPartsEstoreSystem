package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.UserDTO;
import com.myproject.autopartsestoresystem.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Miroslav Kološnjaji
 */
public interface UserService extends UserDetailsService, CrudService<UserDTO, Long> {
}
