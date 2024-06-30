package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.PriceDTO;
import com.myproject.autopartsestoresystem.exception.service.PriceNotFoundException;
import com.myproject.autopartsestoresystem.mapper.PriceMapper;
import com.myproject.autopartsestoresystem.model.Price;
import com.myproject.autopartsestoresystem.model.PriceId;
import com.myproject.autopartsestoresystem.repository.PriceRepository;
import com.myproject.autopartsestoresystem.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final PriceMapper priceMapper;

    @Override
    public PriceDTO save(PriceDTO priceDTO) {

       Price price = priceRepository.save(priceMapper.priceDTOToPrice(priceDTO));

       return priceMapper.priceToPriceDTO(price);
    }

    @Override
    public PriceDTO update(PriceId priceId, PriceDTO priceDTO) {
        AtomicReference<Price> priceRef = new AtomicReference<>();

        priceRepository.getPriceByIdAndDateModified(priceId, priceDTO.getDateModified())
                .ifPresent(price -> {
                    price.setPrice(priceDTO.getPrice());
                    price.setCurrency(priceDTO.getCurrency());

                   priceRef.set(priceRepository.save(price));

                });

        return priceMapper.priceToPriceDTO(priceRef.get());
    }

    @Override
    public List<PriceDTO> getAll() {
        throw new UnsupportedOperationException("Get all prices operation is not supported");
    }

    @Override
    public PriceDTO getById(PriceId priceId) {
        throw new UnsupportedOperationException("Get price by ID operation is not supported");
    }

    @Override
    public List<PriceDTO> getAllPricesByPriceId(PriceId priceId) {
        return priceRepository.getPricesById(priceId).stream().map(priceMapper::priceToPriceDTO).collect(Collectors.toList());
    }

    @Override
    public void delete(PriceId priceId) {
        throw new UnsupportedOperationException("Delete operation is not supported");
    }

    @Override
    public PriceDTO getPriceByPriceIdAndLastModifiedDate(PriceId priceId, LocalDateTime lastModifiedDate) {
       Price price = priceRepository.getPriceByIdAndDateModified(priceId, lastModifiedDate).orElseThrow(() -> new PriceNotFoundException("Price for part ID: " + priceId.getId() +
                "doesn't exists"));

       return priceMapper.priceToPriceDTO(price);
    }
}
