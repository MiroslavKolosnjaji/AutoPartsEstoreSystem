package com.myproject.autopartsestoresystem.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "part")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "part_id")
    private Long id;

    private String partNumber;
    private String partName;
    private String description;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Price> prices;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "part_group", nullable = false)
    private PartGroup partGroup;

    @ManyToMany(mappedBy = "parts")
    @JsonIgnore
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "part")
    private List<PurchaseOrderItem> purchaseOrderItems;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Part part)) return false;

        return Objects.equals(id, part.id) && Objects.equals(partNumber, part.partNumber) && Objects.equals(partName, part.partName) && Objects.equals(description, part.description) && Objects.equals(prices, part.prices) && Objects.equals(partGroup, part.partGroup) && Objects.equals(vehicles, part.vehicles) && Objects.equals(purchaseOrderItems, part.purchaseOrderItems);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(partNumber);
        result = 31 * result + Objects.hashCode(partName);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(prices);
        result = 31 * result + Objects.hashCode(partGroup);
        result = 31 * result + Objects.hashCode(purchaseOrderItems);
        return result;
    }
}
