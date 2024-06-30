package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.PartGroup;
import com.myproject.autopartsestoresystem.model.Price;
import com.myproject.autopartsestoresystem.model.Vehicle;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
public class PartDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank(message = "Part number cannot be blank!")
    @Size(max = 20, message = "Part number cannot be longer than 20 characters" )
    private String partNumber;

    @NotBlank(message = "Part name cannot be blank!")
    private String partName;

    @Size(max = 255, message = "Description cannot be longer than 255 characters")
    private String description;

    @NotNull(message = "Price list cannot be null")
    private List<Price> prices;

    @NotNull(message = "Part group cannot be null")
    private PartGroup partGroup;

    @NotNull(message = "Vehicle list cannot be null")
    private List<Vehicle> vehicles;

}
