package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface ModelRepository extends JpaRepository<Model, Long> {
}
