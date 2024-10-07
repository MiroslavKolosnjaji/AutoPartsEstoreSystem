package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
