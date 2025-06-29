package com.myproject.autopartsestoresystem.users.repository;

import com.myproject.autopartsestoresystem.users.entity.Role;
import com.myproject.autopartsestoresystem.users.entity.RoleName;
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
