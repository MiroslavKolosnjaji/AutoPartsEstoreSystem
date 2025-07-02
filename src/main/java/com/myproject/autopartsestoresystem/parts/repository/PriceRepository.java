package com.myproject.autopartsestoresystem.parts.repository;

import com.myproject.autopartsestoresystem.parts.entity.Price;
import com.myproject.autopartsestoresystem.parts.entity.PriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface PriceRepository extends JpaRepository<Price, PriceId> {

    @Query("SELECT MAX(p.id.priceId) FROM Price p WHERE p.id.partId = :id ")
    Long findMaxPriceId(Long id);

    List<Price> getPricesById(PriceId priceId);
    Optional<Price> getPriceByIdAndDateModified(PriceId priceId , LocalDateTime dateModified);
}
