package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Miroslav Kološnjaji
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
}
