package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.PriceDTO;
import com.myproject.autopartsestoresystem.model.Price;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface PriceMapper {

    PriceDTO priceToPriceDTO(Price price);
    Price priceDTOToPrice(PriceDTO priceDTO);
    List<PriceDTO> priceListToPriceDTOList(List<Price> prices);
    List<Price> priceDTOListToPrice(List<PriceDTO> priceDTOList);
}
