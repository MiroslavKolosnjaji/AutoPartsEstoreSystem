package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.UserDTO;
import com.myproject.autopartsestoresystem.model.RoleName;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Miroslav Kološnjaji
 */
public interface UserService extends UserDetailsService, CrudService<UserDTO, Long> {

    void updateUserAuthority(String username, RoleName authority, UserAuthorityUpdateStatus updateStatus);
}
