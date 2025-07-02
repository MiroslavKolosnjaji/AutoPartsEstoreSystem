package com.myproject.autopartsestoresystem.brands.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.myproject.autopartsestoresystem.models.entity.Model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brand_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Model> models;
}
