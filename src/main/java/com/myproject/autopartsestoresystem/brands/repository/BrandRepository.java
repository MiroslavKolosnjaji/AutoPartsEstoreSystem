package com.myproject.autopartsestoresystem.brands.repository;

import com.myproject.autopartsestoresystem.brands.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    Optional<Brand> findByName(String name);
}
