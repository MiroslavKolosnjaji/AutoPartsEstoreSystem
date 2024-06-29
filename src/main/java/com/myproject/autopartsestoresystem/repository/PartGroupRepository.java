package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.PartGroup;
import com.myproject.autopartsestoresystem.model.PartGroupType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface PartGroupRepository extends JpaRepository<PartGroup, Long> {
    Optional<PartGroup> findByName(PartGroupType partGroupType);
}
