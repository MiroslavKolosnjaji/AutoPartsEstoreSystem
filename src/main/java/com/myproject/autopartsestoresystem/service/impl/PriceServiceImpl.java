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

        PriceId priceId = new PriceId(priceDTO.getId().getPartId(), generateNextPriceId(priceRepository.findMaxPriceId(priceDTO.getId().getPartId())));
        priceDTO.setId(priceId);

        if (priceDTO.getDateModified() == null)
            priceDTO.setDateModified(LocalDateTime.now());

        Price price = priceRepository.save(priceMapper.priceDTOToPrice(priceDTO));

        return priceMapper.priceToPriceDTO(price);
    }

    @Override
    public PriceDTO update(PriceId priceId, PriceDTO priceDTO) {

        PriceUpdateStatus updateStatus = getPriceUpdateStatus(priceId, priceDTO.getDateModified());


        switch (updateStatus) {

            case NEW_PRICE -> {
                return save(priceDTO);
            }

            case UPDATE_EXISTING_PRICE -> {
                return updateExistingPrice(priceId, priceDTO);
            }

            default -> {
                return null;
            }
        }
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
    public Optional<PriceDTO> getPriceByPriceIdAndLastModifiedDate(PriceId priceId, LocalDateTime lastModifiedDate) {

        Optional<Price> price = priceRepository.getPriceByIdAndDateModified(priceId, lastModifiedDate);

        return price.map(priceMapper::priceToPriceDTO);

    }

    private PriceUpdateStatus getPriceUpdateStatus(PriceId priceId, LocalDateTime lastModifiedDate) {

        Optional<PriceDTO> lastPrice = getPriceByPriceIdAndLastModifiedDate(priceId, lastModifiedDate);

        if (lastPrice.isPresent())
            return lastPrice.get().getId().equals(priceId) ? PriceUpdateStatus.UPDATE_EXISTING_PRICE : PriceUpdateStatus.NO_CHANGES;

        return PriceUpdateStatus.NEW_PRICE;
    }

    private PriceDTO updateExistingPrice(PriceId priceId, PriceDTO priceDTO) {
        AtomicReference<Price> priceRef = new AtomicReference<>();

        priceRepository.getPriceByIdAndDateModified(priceId, priceDTO.getDateModified())
                .ifPresentOrElse(price -> {
                    price.setPrice(priceDTO.getPrice());
                    price.setCurrency(priceDTO.getCurrency());

                    priceRef.set(priceRepository.save(price));

                }, () -> {
                    throw new PriceNotFoundException("Price not found for update");
                });

        return priceMapper.priceToPriceDTO(priceRef.get());
    }
}
