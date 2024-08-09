package com.myproject.autopartsestoresystem.dto;

import com.myproject.autopartsestoresystem.model.City;
import com.myproject.autopartsestoresystem.model.Invoice;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class StoreDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank(message = "Store name required")
    private String name;

    @Pattern(regexp = "\\+?[0-9]+", message = "Invalid phone number format")
    private String phoneNumber;

    @NotBlank(message = "Email required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "City cannot be null")
    private City city;

    private List<Invoice> invoices;
}
