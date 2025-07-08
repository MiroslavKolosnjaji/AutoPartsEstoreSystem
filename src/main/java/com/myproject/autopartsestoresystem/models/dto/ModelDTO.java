package com.myproject.autopartsestoresystem.models.dto;

import com.myproject.autopartsestoresystem.brands.entity.Brand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * @author Miroslav Kološnjaji
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelDTO {

    @NotNull(message = "Id is required")
    private Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Brand is required")
    private Brand brand;


}
