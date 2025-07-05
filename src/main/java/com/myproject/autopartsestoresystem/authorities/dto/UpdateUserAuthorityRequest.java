package com.myproject.autopartsestoresystem.authorities.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserAuthorityRequest {

    @NotBlank(message = "Authority required")
    private String authority;

    @NotBlank(message = "Update status required")
    private String updateStatus;
}
