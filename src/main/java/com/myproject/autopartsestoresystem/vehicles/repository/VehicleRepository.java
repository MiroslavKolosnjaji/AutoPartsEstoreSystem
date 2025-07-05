package com.myproject.autopartsestoresystem.vehicles.repository;

import com.myproject.autopartsestoresystem.vehicles.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Miroslav Kološnjaji
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
}
