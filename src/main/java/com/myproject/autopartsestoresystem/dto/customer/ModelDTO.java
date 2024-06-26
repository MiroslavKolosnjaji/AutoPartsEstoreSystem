package com.myproject.autopartsestoresystem.dto.customer;

import com.myproject.autopartsestoresystem.model.Brand;
import com.myproject.autopartsestoresystem.model.ModelId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull
    private ModelId id;

    @NotNull
    private Brand brand;


}
