package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Role;
import com.myproject.autopartsestoresystem.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
    List<Role> findByNameIn(List<String> names);
}
