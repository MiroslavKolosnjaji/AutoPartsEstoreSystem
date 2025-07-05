package com.myproject.autopartsestoresystem.cities.mapper;

import com.myproject.autopartsestoresystem.cities.dto.CityDTO;
import com.myproject.autopartsestoresystem.cities.entity.City;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface CityMapper {

    CityDTO cityToCityDTO(City city);
    City cityDTOToCity(CityDTO cityDTO);
}
