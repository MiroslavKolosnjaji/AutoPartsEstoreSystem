package com.myproject.autopartsestoresystem.brands.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.sql.Update;

/**
 * @author Miroslav Kološnjaji
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandDTO {

    @NotNull(groups = Update.class)
    private Integer id;

    @NotBlank(message = "Brand name cannot be blank")
    private String name;
}
