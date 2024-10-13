package com.myproject.autopartsestoresystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

/**
 * @author Miroslav Kološnjaji
 */
@Getter
@Setter
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
