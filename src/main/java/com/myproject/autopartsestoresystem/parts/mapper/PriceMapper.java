package com.myproject.autopartsestoresystem.parts.mapper;

import com.myproject.autopartsestoresystem.parts.dto.PriceDTO;
import com.myproject.autopartsestoresystem.parts.entity.Price;
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
