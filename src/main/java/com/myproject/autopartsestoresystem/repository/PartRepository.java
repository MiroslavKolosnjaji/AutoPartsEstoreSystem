package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface PartRepository extends JpaRepository<Part, Long> {

    Optional<Part> findByPartNameAndBrand(String partName, String BrandName);
}
