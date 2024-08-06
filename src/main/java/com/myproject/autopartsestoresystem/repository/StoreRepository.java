package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Miroslav Kološnjaji
 */
public interface StoreRepository extends JpaRepository<Store, Long> {
}
