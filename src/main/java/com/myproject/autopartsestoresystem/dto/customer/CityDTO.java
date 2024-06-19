package com.myproject.autopartsestoresystem.dto.customer;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CityDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank(message = "City name cannot be blank")
    @Size(max = 120, message = "City name cannot be longer than 120 characters")
    private String name;


    @Size(min = 5, max = 5, message = "Zip code must contain 5 digits")
    @Digits(integer = 5, fraction = 0, message = "Zip code must contain 5 digits")
    private String zipCode;
}
