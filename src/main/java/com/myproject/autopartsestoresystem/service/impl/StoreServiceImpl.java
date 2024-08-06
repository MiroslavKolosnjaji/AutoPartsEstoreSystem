package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.StoreDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityAlreadyExistsException;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.StoreNotFoundException;
import com.myproject.autopartsestoresystem.mapper.StoreMapper;
import com.myproject.autopartsestoresystem.model.Store;
import com.myproject.autopartsestoresystem.repository.StoreRepository;
import com.myproject.autopartsestoresystem.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;


    @Override
    public StoreDTO save(StoreDTO storeDTO) {

        Store saved = storeRepository.save(storeMapper.storeDTOToStore(storeDTO));

        return storeMapper.storeToStoreDTO(saved);
    }

    @Override
    public StoreDTO update(Long id, StoreDTO storeDTO) throws StoreNotFoundException {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new StoreNotFoundException("Store not found"));

        store.setName(storeDTO.getName());
        store.setPhoneNumber(storeDTO.getPhoneNumber());
        store.setEmail(storeDTO.getEmail());
        store.setCity(storeDTO.getCity());

        Store updated = storeRepository.save(store);

        return storeMapper.storeToStoreDTO(updated);
    }

    @Override
    public List<StoreDTO> getAll() {
        return storeRepository.findAll().stream().map(storeMapper::storeToStoreDTO).toList();
    }

    @Override
    public StoreDTO getById(Long id) throws StoreNotFoundException {

        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new StoreNotFoundException("Store not found"));

        return storeMapper.storeToStoreDTO(store);
    }

    @Override
    public void delete(Long id) throws StoreNotFoundException {

        if (!storeRepository.existsById(id))
            throw new StoreNotFoundException("Store not found");

        storeRepository.deleteById(id);
    }
}
