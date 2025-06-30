package com.myproject.autopartsestoresystem.brands.mapper;

import com.myproject.autopartsestoresystem.brands.dto.BrandDTO;
import com.myproject.autopartsestoresystem.brands.entity.Brand;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface BrandMapper {

    BrandDTO brandToBrandDTO(Brand brand);
    Brand brandDTOToBrand(BrandDTO brandDTO);
}
