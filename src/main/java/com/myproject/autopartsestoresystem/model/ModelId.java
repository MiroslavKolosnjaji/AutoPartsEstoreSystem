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
public class ModelId {

    @NotNull(groups = Update.class)
    @Column(name = "model_id")
    private Long id;

    @NotBlank
    @Size(max = 50, message = "Name cannot be longer than 50 characters")
    @Column(name = "name", insertable = false, updatable = false)
    private String name;
}
