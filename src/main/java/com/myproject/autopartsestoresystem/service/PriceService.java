package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.PriceDTO;

import com.myproject.autopartsestoresystem.model.PriceId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface PriceService extends CrudService<PriceDTO, PriceId> {

    List<PriceDTO> getAllPricesByPriceId(PriceId priceId);
    PriceDTO getPriceByPriceIdAndLastModifiedDate(PriceId priceId, LocalDateTime lastModifiedDate);
}
