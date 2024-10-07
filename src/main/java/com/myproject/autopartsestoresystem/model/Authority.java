package com.myproject.autopartsestoresystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Miroslav Kološnjaji
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String permission;

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;
}
