package com.myproject.autopartsestoresystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PriceId {

    @NotNull(groups = Update.class, message = "price id cannot be null")
    @Column(name = "part_id")
    private Long id;

    @NotBlank(message = "part name cannot be blank")
    @Size(max = 120, message = "part name cannot be longer than 120 characters")
    private String partName;
}
