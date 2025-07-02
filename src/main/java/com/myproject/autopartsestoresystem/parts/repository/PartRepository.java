package com.myproject.autopartsestoresystem.parts.repository;

import com.myproject.autopartsestoresystem.parts.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface PartRepository extends JpaRepository<Part, Long> {


    @Query("SELECT p FROM Part p WHERE p.id IN :partIds")
    Optional<List<Part>> getSelectedParts(@Param("partIds") List<Long> partIds);

}
