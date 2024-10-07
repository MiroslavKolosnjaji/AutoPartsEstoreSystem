package com.myproject.autopartsestoresystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

/**
 * @author Miroslav Kološnjaji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @Singular
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_authority",
    joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private Set<Authority> authorities;

}
