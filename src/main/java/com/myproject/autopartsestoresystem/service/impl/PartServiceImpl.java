package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.PartDTO;
import com.myproject.autopartsestoresystem.exception.service.PartNotFoundException;
import com.myproject.autopartsestoresystem.mapper.PartMapper;
import com.myproject.autopartsestoresystem.model.Part;
import com.myproject.autopartsestoresystem.model.Price;
import com.myproject.autopartsestoresystem.model.PriceId;
import com.myproject.autopartsestoresystem.repository.PartRepository;
import com.myproject.autopartsestoresystem.service.PartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;
    private final PartMapper partMapper;

    @Override
    @Transactional
    public PartDTO save(PartDTO partDTO) {


        Part saved = partRepository.save(partMapper.partDTOToPart(partDTO));

        return partMapper.partToPartDTO(saved);
    }

    @Override
    @Transactional
    public PartDTO update(Long id, PartDTO partDTO) {

        Part part = partRepository.findById(id).orElseThrow(() -> new PartNotFoundException("Part not found"));

        part.setPartName(partDTO.getPartName());
        part.setPartGroup(partDTO.getPartGroup());
        part.setPartNumber(partDTO.getPartNumber());
        part.setVehicles(partDTO.getVehicles());
        updatePrice(part, partDTO.getPrices());

        partRepository.save(part);
        return partMapper.partToPartDTO(part);
    }

    private void updatePrice(Part part, List<Price> prices) {

        Map<PriceId, Price> priceMap = prices.stream().collect(Collectors.toMap(Price::getId, p -> p));

        for (Price price : prices) {

            Price existingPrice = priceMap.get(price.getId());

            if (existingPrice != null) {
                price.setPrice(price.getPrice());
                existingPrice.setCurrency(price.getCurrency());
            } else {
                part.getPrices().add(price);
            }
        }
    }

    @Override
    public List<PartDTO> getSelectedParts(List<Long> selectedPartIds) {

        List<Part> parts = partRepository.getSelectedParts(selectedPartIds)
                .orElseThrow(() -> new PartNotFoundException("Selected parts not found"));

        return partMapper.partsToPartDTOs(parts);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartDTO> getAll() {
        return partRepository.findAll().stream().map(partMapper::partToPartDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PartDTO getById(Long id) {
        Part part = partRepository.findById(id).orElseThrow(() -> new PartNotFoundException("Part not found"));
        return partMapper.partToPartDTO(part);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if (!partRepository.existsById(id))
            throw new PartNotFoundException("Part not found");

        partRepository.deleteById(id);
    }
}
