package com.myproject.autopartsestoresystem.stores.service;

import com.myproject.autopartsestoresystem.common.service.CrudService;
import com.myproject.autopartsestoresystem.stores.dto.StoreDTO;

/**
 * @author Miroslav Kološnjaji
 */
public interface StoreService extends CrudService<StoreDTO, Long> {

    @Override
    StoreDTO save(StoreDTO storeDTO);
}
