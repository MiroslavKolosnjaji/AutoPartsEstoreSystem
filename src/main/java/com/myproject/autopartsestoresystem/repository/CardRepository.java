package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Miroslav Kološnjaji
 */
public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByCardNumber(String cardNumber);
    List<Card> findByCardHolder(String cardHolder);
}
