package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.City;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.sql.Update;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CustomerDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, message = "Last name must have minimum 2 characters")
    private String lastName;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "\\+?[0-9]+", message = "Invalid phone number format")
    private String phone;

    @NotNull(message = "City cannot be null")
    private City city;
}
