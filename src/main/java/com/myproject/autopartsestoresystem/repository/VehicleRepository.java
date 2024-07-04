package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}
