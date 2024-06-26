package com.myproject.autopartsestoresystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JoinColumn(name = "brand_id", insertable = false, updatable = false)
    private Brand brand;

//    @Column(name = "name", nullable = false, length = 50)
//    private String name;

}
