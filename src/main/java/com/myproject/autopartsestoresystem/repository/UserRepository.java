package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<Void> deleteByUsername(String username);
}
