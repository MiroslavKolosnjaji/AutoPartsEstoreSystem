package com.myproject.autopartsestoresystem.stores.repository;

import com.myproject.autopartsestoresystem.stores.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface StoreRepository extends JpaRepository<Store, Long> {
}
