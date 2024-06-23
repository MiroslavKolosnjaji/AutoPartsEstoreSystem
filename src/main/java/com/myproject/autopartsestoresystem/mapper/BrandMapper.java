package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.customer.BrandDTO;
import com.myproject.autopartsestoresystem.model.Brand;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface BrandMapper {

    BrandDTO brandToBrandDTO(Brand brand);
    Brand brandDTOToBrand(BrandDTO brandDTO);
}
