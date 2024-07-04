package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.Brand;
import com.myproject.autopartsestoresystem.model.Model;
import com.myproject.autopartsestoresystem.model.Part;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank(message = "Engine type cannot be blank")
    @Size(max = 120, message = "Engine type description cannot be longer than 120 characters")
    private String engineType;

    @NotBlank(message = "Series cannot be blank")
    @Size(max = 120, message = "Series description cannot be longer than 120 characters")
    private String series;

    @NotNull(message = "Model cannot be null")
    private Model model;

    @NotNull(message = "Brand cannot be null")
    private Brand brand;

    @NotNull(message = "Part list cannot be null")
    private List<Part> parts;

}
