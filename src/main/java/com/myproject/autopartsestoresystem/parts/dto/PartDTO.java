package com.myproject.autopartsestoresystem.parts.dto;

import com.myproject.autopartsestoresystem.parts.entity.PartGroup;
import com.myproject.autopartsestoresystem.parts.entity.Price;
import com.myproject.autopartsestoresystem.vehicles.entity.Vehicle;
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

    @NotBlank(message = "Part number cannot be blank")
    @Size(max = 20, message = "Part number cannot be longer than 20 characters" )
    private String partNumber;

    @NotBlank(message = "Part name cannot be blank")
    @Size(max = 120, message = "Part name cannot be longer than 120 characters")
    private String partName;

    @Size(max = 255, message = "Description cannot be longer than 255 characters")
    private String description;

    @NotNull(groups = Update.class,message = "Price list cannot be null")
    private List<Price> prices;

    @NotNull(message = "Part group cannot be null")
    private PartGroup partGroup;

    @NotNull(message = "Vehicle list cannot be null")
    private List<Vehicle> vehicles;

}
