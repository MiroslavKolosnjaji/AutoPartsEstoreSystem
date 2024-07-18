package com.myproject.autopartsestoresystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @NotBlank(message = "Email is required")
    private String username;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password length must be at least 8 characters")
    private String password;

    private boolean enabled;

    @NotNull(message = "User roles cannot be null")
    private Set<RoleDTO> roles;

}
