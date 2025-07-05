package com.myproject.autopartsestoresystem.users.service;

import com.myproject.autopartsestoresystem.service.CrudService;
import com.myproject.autopartsestoresystem.users.dto.UserDTO;
import com.myproject.autopartsestoresystem.users.exception.RoleNotFoundException;
import com.myproject.autopartsestoresystem.users.exception.UsernameAlreadyExistsException;
import com.myproject.autopartsestoresystem.users.entity.RoleName;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Miroslav Kološnjaji
 */
public interface UserService extends UserDetailsService, CrudService<UserDTO, Long> {

    UserDTO saveUser(UserDTO userDTO) throws UsernameAlreadyExistsException, RoleNotFoundException;
    void updateUserAuthority(Long userId, RoleName authority, UserAuthorityUpdateStatus updateStatus) throws RoleNotFoundException;
}
