package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.customer.CityDTO;
import com.myproject.autopartsestoresystem.model.City;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface CityMapper {

    CityDTO cityToCityDTO(City city);
    City cityDTOToCity(CityDTO cityDTO);
}
