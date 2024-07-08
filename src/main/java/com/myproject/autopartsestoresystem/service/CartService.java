package com.myproject.autopartsestoresystem.service;

import com.myproject.autopartsestoresystem.dto.CartDTO;
import com.myproject.autopartsestoresystem.model.CartStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Miroslav Kološnjaji
 */
public interface CartService extends CrudService<CartDTO, Long> {

    CartDTO findByCartNumber(UUID cartNumber);
    Optional<List<CartDTO>> findByCustomerId(Long customerId);
    void updateCartStatus(UUID cartNumber, CartStatus cartStatus);


}
