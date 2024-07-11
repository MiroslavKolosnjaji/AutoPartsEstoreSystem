package com.myproject.autopartsestoresystem.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String engineType;
    private String series;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "model_brand_id", referencedColumnName = "brand_id"),
            @JoinColumn(name = "model_name", referencedColumnName = "name")
    })
    private Model model;

    @ManyToMany
    @JoinTable(name = "vehicle_part", joinColumns = @JoinColumn(name = "vehicle_id"), inverseJoinColumns = @JoinColumn(name = "part_id"))
    private List<Part> parts;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle vehicle)) return false;

        return Objects.equals(id, vehicle.id) && Objects.equals(engineType, vehicle.engineType) && Objects.equals(series, vehicle.series) && Objects.equals(model, vehicle.model) && Objects.equals(parts, vehicle.parts);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(engineType);
        result = 31 * result + Objects.hashCode(series);
        result = 31 * result + Objects.hashCode(model);
        return result;
    }
}
