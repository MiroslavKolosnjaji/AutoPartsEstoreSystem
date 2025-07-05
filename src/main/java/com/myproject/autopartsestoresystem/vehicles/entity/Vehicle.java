package com.myproject.autopartsestoresystem.vehicles.entity;

import com.myproject.autopartsestoresystem.models.entity.Model;
import com.myproject.autopartsestoresystem.parts.entity.Part;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "vehicle_id")
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
    @EqualsAndHashCode.Exclude
    private List<Part> parts;

}
