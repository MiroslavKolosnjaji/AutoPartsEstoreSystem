package com.myproject.autopartsestoresystem.stores.mapper;

import com.myproject.autopartsestoresystem.stores.dto.StoreDTO;
import com.myproject.autopartsestoresystem.stores.entity.Store;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface StoreMapper {

    StoreDTO storeToStoreDTO(Store store);
    Store storeDTOToStore(StoreDTO storeDTO);
}
