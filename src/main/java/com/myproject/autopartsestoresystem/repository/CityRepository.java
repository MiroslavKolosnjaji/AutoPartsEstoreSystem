package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface CityRepository  extends JpaRepository<City, Long> {
}
