package com.myproject.autopartsestoresystem.authorities.repository;

import com.myproject.autopartsestoresystem.authorities.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
