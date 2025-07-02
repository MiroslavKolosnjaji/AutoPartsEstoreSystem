package com.myproject.autopartsestoresystem.parts.service.impl;

import com.myproject.autopartsestoresystem.parts.dto.PartDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.parts.exception.PartNotFoundException;
import com.myproject.autopartsestoresystem.parts.mapper.PartMapper;
import com.myproject.autopartsestoresystem.parts.entity.Part;
import com.myproject.autopartsestoresystem.parts.entity.Price;
import com.myproject.autopartsestoresystem.parts.entity.PriceId;
import com.myproject.autopartsestoresystem.parts.repository.PartRepository;
import com.myproject.autopartsestoresystem.parts.service.PartService;
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
    public PartDTO save(PartDTO partDTO) throws EntityAlreadyExistsException {

        Part saved = partRepository.save(partMapper.partDTOToPart(partDTO));

        return partMapper.partToPartDTO(saved);
    }

    @Override
    @Transactional
    public PartDTO update(Long id, PartDTO partDTO) throws PartNotFoundException {

        Part part = partRepository.findById(id)
                .orElseThrow(() -> new PartNotFoundException("Part not found"));

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
    public List<PartDTO> getSelectedParts(List<Long> selectedPartIds) throws PartNotFoundException {

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
    public PartDTO getById(Long id) throws PartNotFoundException {
        Part part = partRepository.findById(id)
                .orElseThrow(() -> new PartNotFoundException("Part not found"));

        return partMapper.partToPartDTO(part);
    }

    @Override
    @Transactional
    public void delete(Long id) throws PartNotFoundException {

        if (!partRepository.existsById(id))
            throw new PartNotFoundException("Part not found");

        partRepository.deleteById(id);
    }
}
