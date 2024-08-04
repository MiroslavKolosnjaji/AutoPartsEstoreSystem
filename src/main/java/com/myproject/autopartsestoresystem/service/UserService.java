package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.UserDTO;
import com.myproject.autopartsestoresystem.exception.service.RoleNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.UsernameAlreadyExistsException;
import com.myproject.autopartsestoresystem.model.RoleName;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Miroslav Kološnjaji
 */
public interface UserService extends UserDetailsService, CrudService<UserDTO, Long> {

    UserDTO saveUser(UserDTO userDTO) throws UsernameAlreadyExistsException, RoleNotFoundException;
    void updateUserAuthority(Long userId, RoleName authority, UserAuthorityUpdateStatus updateStatus) throws RoleNotFoundException;
}
