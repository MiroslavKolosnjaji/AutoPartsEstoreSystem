package com.myproject.autopartsestoresystem.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.myproject.autopartsestoresystem.brands.entity.Brand;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "models")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    @ToString.Exclude
    private Brand brand;

}
