package com.myproject.autopartsestoresystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "model")
public class Model {

    @EmbeddedId
    private ModelId id;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "brand_id")
    @JsonBackReference
    private Brand brand;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Model model)) return false;

        return Objects.equals(id, model.id) && Objects.equals(brand, model.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
