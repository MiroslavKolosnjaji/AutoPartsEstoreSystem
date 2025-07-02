package com.myproject.autopartsestoresystem.parts.repository;

import com.myproject.autopartsestoresystem.parts.entity.PartGroup;
import com.myproject.autopartsestoresystem.parts.entity.PartGroupType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface PartGroupRepository extends JpaRepository<PartGroup, Long> {
    Optional<PartGroup> findByName(PartGroupType partGroupType);
}
