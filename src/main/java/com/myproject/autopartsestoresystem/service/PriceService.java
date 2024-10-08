package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.PriceDTO;

import com.myproject.autopartsestoresystem.exception.service.PriceNotFoundException;
import com.myproject.autopartsestoresystem.model.PriceId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface PriceService extends CrudService<PriceDTO, PriceId> {

    List<PriceDTO> getAllPricesByPriceId(PriceId priceId);
    Optional<PriceDTO> getPriceByPriceIdAndLastModifiedDate(PriceId priceId, LocalDateTime lastModifiedDate) throws PriceNotFoundException;

    default Long generateNextPriceId(Long maxPriceId){
        return (maxPriceId == null || maxPriceId == 0) ? 1 : maxPriceId + 1;
    }
}
