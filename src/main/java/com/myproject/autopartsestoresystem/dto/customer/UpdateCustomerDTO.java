package com.myproject.autopartsestoresystem.dto.customer;

import com.myproject.autopartsestoresystem.model.City;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCustomerDTO{

    @NotNull
    private Long id;

    @NotNull
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotNull
    @Size(min = 2, message = "Last name must have minimum 2 characters")
    private String lastName;

    @NotNull(message = "Address cannot be null")
    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Phone number cannot be null")
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "\\+?[0-9]+", message = "Invalid phone number format")
    private String phone;

    @NotNull(message = "City cannot be null")
    private City city;
}
