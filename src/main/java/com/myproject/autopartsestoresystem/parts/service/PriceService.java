package com.myproject.autopartsestoresystem.parts.service;

import com.myproject.autopartsestoresystem.parts.dto.PriceDTO;

import com.myproject.autopartsestoresystem.parts.exception.PriceNotFoundException;
import com.myproject.autopartsestoresystem.parts.entity.PriceId;
import com.myproject.autopartsestoresystem.service.CrudService;

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
