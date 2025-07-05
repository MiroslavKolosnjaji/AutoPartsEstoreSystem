package com.myproject.autopartsestoresystem.cities.repository;

import com.myproject.autopartsestoresystem.cities.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface CityRepository  extends JpaRepository<City, Long> {

    Optional<City> findByNameAndZipCode(String name, String zipCode);
    Optional<City> findByName(String name);
}
