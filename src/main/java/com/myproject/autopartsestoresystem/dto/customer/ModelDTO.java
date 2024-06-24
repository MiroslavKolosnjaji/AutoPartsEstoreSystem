package com.myproject.autopartsestoresystem.dto.customer;

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
public class ModelDTO {

    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    private String name;
}
