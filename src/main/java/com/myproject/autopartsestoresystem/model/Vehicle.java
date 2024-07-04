package com.myproject.autopartsestoresystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private Model modelName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", insertable = false, updatable = false)
    private Brand brand;

    @ManyToMany
    @JoinTable(name = "vehicle_part", joinColumns = @JoinColumn(name = "vehicle_id"), inverseJoinColumns = @JoinColumn(name = "part_id"))
    private List<Part> parts;
}
