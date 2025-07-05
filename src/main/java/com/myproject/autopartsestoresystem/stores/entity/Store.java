package com.myproject.autopartsestoresystem.stores.entity;

import com.myproject.autopartsestoresystem.cities.entity.City;
import com.myproject.autopartsestoresystem.invoices.entity.Invoice;
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
@Table(name = "store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    private String name;
    private String phoneNumber;
    private String email;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices;
}
