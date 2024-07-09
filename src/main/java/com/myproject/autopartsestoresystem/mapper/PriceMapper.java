package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.PriceDTO;
import com.myproject.autopartsestoresystem.model.Price;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface PriceMapper {

    PriceDTO priceToPriceDTO(Price price);
    Price priceDTOToPrice(PriceDTO priceDTO);

    @IterableMapping(elementTargetType = PriceDTO.class)
    List<PriceDTO> priceListToPriceDTOList(List<Price> prices);

    @IterableMapping(elementTargetType = Price.class)
    List<Price> priceDTOListToPrice(List<PriceDTO> priceDTOList);
}
