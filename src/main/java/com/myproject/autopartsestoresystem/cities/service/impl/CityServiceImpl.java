package com.myproject.autopartsestoresystem.cities.service.impl;

import com.myproject.autopartsestoresystem.cities.dto.CityDTO;
import com.myproject.autopartsestoresystem.cities.exception.CityAlreadyExistsException;
import com.myproject.autopartsestoresystem.cities.exception.CityNotFoundException;
import com.myproject.autopartsestoresystem.cities.mapper.CityMapper;
import com.myproject.autopartsestoresystem.cities.entity.City;
import com.myproject.autopartsestoresystem.cities.repository.CityRepository;
import com.myproject.autopartsestoresystem.cities.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public CityDTO save(CityDTO cityDTO) throws CityAlreadyExistsException {

        if (cityRepository.findByNameAndZipCode(cityDTO.getName(), cityDTO.getZipCode()).isPresent())
            throw new CityAlreadyExistsException();

        City saved = cityRepository.save(cityMapper.cityDTOToCity(cityDTO));
        return cityMapper.cityToCityDTO(saved);
    }

    @Override
    public CityDTO update(Long id, CityDTO cityDTO) throws CityNotFoundException {

        City city = cityRepository.findById(id).orElseThrow(CityNotFoundException::new);

        city.setName(cityDTO.getName());
        city.setZipCode(cityDTO.getZipCode());

        City updated = cityRepository.save(city);
        return cityMapper.cityToCityDTO(updated);
    }

    @Override
    public List<CityDTO> getAll() {
        return cityRepository.findAll().stream().map(cityMapper::cityToCityDTO).collect(Collectors.toList());
    }

    @Override
    public CityDTO getById(Long id) throws CityNotFoundException {

        City city = cityRepository.findById(id).orElseThrow(CityNotFoundException::new);

        return cityMapper.cityToCityDTO(city);
    }

    @Override
    public void delete(Long id) throws CityNotFoundException {

        if (!cityRepository.existsById(id))
            throw new CityNotFoundException();

        cityRepository.deleteById(id);
    }
}
