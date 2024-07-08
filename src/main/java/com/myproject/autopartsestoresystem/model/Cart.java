package com.myproject.autopartsestoresystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cartNumber", unique = true, nullable = false)
    private UUID cartNumber;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    private BigDecimal totalAmount;

    @ElementCollection
    @CollectionTable(name = "item", joinColumns = @JoinColumn(name = "cart_id"))
    private Set<Item> items;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;


}
