package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface CityRepository  extends JpaRepository<City, Long> {

    Optional<City> findByNameAndZipCode(String name, String zipCode);
}
