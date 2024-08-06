package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.StoreDTO;

/**
 * @author Miroslav Kološnjaji
 */
public interface StoreService extends CrudService<StoreDTO, Long>{

    @Override
    StoreDTO save(StoreDTO storeDTO);
}
