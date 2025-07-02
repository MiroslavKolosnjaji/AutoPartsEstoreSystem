package com.myproject.autopartsestoresystem.models.dto;

import com.myproject.autopartsestoresystem.brands.entity.Brand;
import com.myproject.autopartsestoresystem.models.entity.ModelId;
import jakarta.validation.Valid;
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
public class ModelDTO {

    @Valid
    @NotNull(message = "Model id can't be null")
    private ModelId id;

    @NotNull(message = "Brand can't be null")
    private Brand brand;


}
