package com.myproject.autopartsestoresystem.users.mapper;

import com.myproject.autopartsestoresystem.users.dto.UserDTO;
import com.myproject.autopartsestoresystem.users.entity.User;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface UserMapper {

    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);
}
