package com.myproject.autopartsestoresystem.models.entity;

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

    @NotNull(groups = Update.class, message = "Id cannot be null")
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name cannot be greater than 50 characters")
    @Column(name = "name")
    private String name;
}
