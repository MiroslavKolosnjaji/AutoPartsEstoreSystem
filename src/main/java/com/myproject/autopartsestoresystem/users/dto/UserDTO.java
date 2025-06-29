package com.myproject.autopartsestoresystem.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.sql.Update;

import java.util.Set;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    @NotNull(groups = Update.class, message = "User ID cannot be null")
    private Long id;

    @NotBlank(message = "Username is required")
    @Email(message = "Username is not valid" )
    private String username;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Your password must be at least 8 characters long")
    private String password;

    private boolean enabled;

    @NotNull(message = "User roles cannot be null")
    private Set<RoleDTO> roles;

}
