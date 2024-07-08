package com.myproject.autopartsestoresystem.repository;

import com.myproject.autopartsestoresystem.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Miroslav Kološnjaji
 */
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByCartNumber(UUID cartNumber);
    Optional<List<Cart>> findByCustomerId(Long customerId);
}
