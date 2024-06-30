package com.myproject.autopartsestoresystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank(message = "Brand name cannot be blank")
    private String name;
}
