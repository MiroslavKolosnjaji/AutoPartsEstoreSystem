package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.StoreDTO;
import com.myproject.autopartsestoresystem.model.Store;
import org.mapstruct.Mapper;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper
public interface StoreMapper {

    StoreDTO storeToStoreDTO(Store store);
    Store storeDTOToStore(StoreDTO storeDTO);
}
