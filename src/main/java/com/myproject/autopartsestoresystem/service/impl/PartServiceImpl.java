package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.PartDTO;
import com.myproject.autopartsestoresystem.dto.PriceDTO;
import com.myproject.autopartsestoresystem.exception.service.PartNotFoundException;
import com.myproject.autopartsestoresystem.mapper.PartMapper;
import com.myproject.autopartsestoresystem.mapper.PriceMapper;
import com.myproject.autopartsestoresystem.model.Part;
import com.myproject.autopartsestoresystem.model.Price;
import com.myproject.autopartsestoresystem.model.PriceId;
import com.myproject.autopartsestoresystem.repository.PartRepository;
import com.myproject.autopartsestoresystem.service.PartService;
import com.myproject.autopartsestoresystem.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class PartServiceImpl implements PartService {


    private final PartRepository partRepository;
    private final PriceService priceService;
    private final PartMapper partMapper;
    private final PriceMapper priceMapper;

    @Override
    @Transactional
    public PartDTO save(PartDTO partDTO) {

        if(partDTO.getPrices().isEmpty())
            throw new IllegalArgumentException("Part must contain at least one price");

        Part saved = partRepository.save(partMapper.partDTOToPart(partDTO));
        priceService.save(priceMapper.priceToPriceDTO(getLastPriceFromPart(saved)));

        return partMapper.partToPartDTO(saved);
    }

    @Override
    @Transactional
    public PartDTO update(Long id, PartDTO partDTO) {

       Part part = partRepository.findById(id).orElseThrow(() -> new PartNotFoundException("Part not found"));

       part.setPartName(partDTO.getPartName());
       part.setPartGroup(partDTO.getPartGroup());
       part.setPartNumber(partDTO.getPartNumber());
       part.setPrices(partDTO.getPrices());
       part.setVehicles(partDTO.getVehicles());

       partRepository.save(part);

       Price lastPrice = getLastPriceFromPart(part);
       Price existingLastPrice = getLastPriceFromPriceHistory(partDTO.getId(), partDTO.getPartName(), getLastPriceFromPart(part).getDateModified());

       if(!lastPrice.equals(existingLastPrice))
            priceService.save(priceMapper.priceToPriceDTO(getLastPriceFromPart(part)));


        return partMapper.partToPartDTO(part);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartDTO> getAll() {
        return partRepository.findAll().stream().map(partMapper::partToPartDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PartDTO getById(Long id) {
      Part part =  partRepository.findById(id).orElseThrow(() -> new PartNotFoundException("Part not found"));
      List<PriceDTO> prices = priceService.getAllPricesByPriceId(new PriceId(part.getId(), part.getPartName()));

      part.setPrices(priceMapper.priceDTOListToPrice(prices));

      return partMapper.partToPartDTO(part);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if(!partRepository.existsById(id))
            throw new PartNotFoundException("Part not found");

        partRepository.deleteById(id);
    }

    private Price getLastPriceFromPart(Part part) {
        return part.getPrices().get(part.getPrices().size() - 1);
    }

    private Price getLastPriceFromPriceHistory(Long id, String partName, LocalDateTime lastModifiedDate) {
        PriceDTO priceDTO = priceService.getPriceByPriceIdAndLastModifiedDate(new PriceId(id, partName), lastModifiedDate);
        return priceMapper.priceDTOToPrice(priceDTO);
    }
}
