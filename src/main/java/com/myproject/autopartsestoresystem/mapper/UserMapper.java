package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.UserDTO;
import com.myproject.autopartsestoresystem.model.User;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface UserMapper {

    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);
}
