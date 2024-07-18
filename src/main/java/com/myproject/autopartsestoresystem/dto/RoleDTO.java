package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {

    private Long id;

    @NotNull(message = "Role name is required")
    private RoleName name;
}
