package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.BrandDTO;
import com.myproject.autopartsestoresystem.exception.service.BrandAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.service.BrandNotFoundException;
import com.myproject.autopartsestoresystem.mapper.BrandMapper;
import com.myproject.autopartsestoresystem.model.Brand;
import com.myproject.autopartsestoresystem.repository.BrandRepository;
import com.myproject.autopartsestoresystem.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;


    @Override
    @Transactional
    public BrandDTO save(BrandDTO brandDTO) throws BrandAlreadyExistsException {

        if (brandRepository.findByName(brandDTO.getName()).isPresent())
            throw new BrandAlreadyExistsException("Brand " + brandDTO.getName() + " already exists");


        Brand saved = brandRepository.save(brandMapper.brandDTOToBrand(brandDTO));

        return brandMapper.brandToBrandDTO(saved);
    }

    @Override
    @Transactional
    public BrandDTO update(Long id, BrandDTO brandDTO) throws BrandNotFoundException {

        Brand brand = brandRepository.findById(id).orElseThrow(BrandNotFoundException::new);

        brand.setName(brandDTO.getName());

        Brand updated = brandRepository.save(brand);

        return brandMapper.brandToBrandDTO(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandDTO> getAll() {
        return brandRepository.findAll().stream().map(brandMapper::brandToBrandDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BrandDTO getById(Long id) throws BrandNotFoundException {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(BrandNotFoundException::new);

        return brandMapper.brandToBrandDTO(brand);
    }

    @Override
    @Transactional
    public void delete(Long id) throws BrandNotFoundException {

        if(!brandRepository.existsById(id))
            throw new BrandNotFoundException("Brand does not exist");

        brandRepository.deleteById(id);
    }
}
