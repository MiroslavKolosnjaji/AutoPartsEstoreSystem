package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Price;
import com.myproject.autopartsestoresystem.model.PriceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface PriceRepository extends JpaRepository<Price, PriceId> {

    List<Price> getPricesById(PriceId priceId);
    Optional<Price> getPriceByIdAndDateModified(PriceId priceId , LocalDateTime dateModified);
}
