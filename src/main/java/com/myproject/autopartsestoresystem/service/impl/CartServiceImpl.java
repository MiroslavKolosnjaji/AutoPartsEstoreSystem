package com.myproject.autopartsestoresystem.service.impl;

import com.myproject.autopartsestoresystem.dto.CartDTO;
import com.myproject.autopartsestoresystem.exception.service.CartNotFoundException;
import com.myproject.autopartsestoresystem.mapper.CartMapper;
import com.myproject.autopartsestoresystem.model.Cart;
import com.myproject.autopartsestoresystem.model.CartStatus;
import com.myproject.autopartsestoresystem.model.Item;
import com.myproject.autopartsestoresystem.repository.CartRepository;
import com.myproject.autopartsestoresystem.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Miroslav Kološnjaji
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;


    @Override
    public CartDTO findByCartNumber(UUID cartNumber) {

        Cart cart = cartRepository.findByCartNumber(cartNumber)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        return cartMapper.cartToCartDTO(cart);
    }


    @Override
    public void updateCartStatus(UUID cartNumber, CartStatus cartStatus) {
        Cart cart = cartRepository.findByCartNumber(cartNumber)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        cart.setStatus(cartStatus);
        cartRepository.save(cart);
    }

    @Override
    public Optional<List<CartDTO>> findByCustomerId(Long customerId) {
       //TODO: Implement this method
        return Optional.empty();
    }

    @Override
    @Transactional
    public CartDTO save(CartDTO cartDTO) {

        cartDTO.setCartNumber(generateCartNumber());
        cartDTO.setStatus(CartStatus.PENDING_PROCESSING);

        Cart cart = cartMapper.cartDTOToCart(cartDTO);

        addOrdinalNumberToItem(cart);

        Cart savedCart = cartRepository.save(cart);

        return cartMapper.cartToCartDTO(savedCart);
    }

    @Override
    @Transactional
    public CartDTO update(Long id, CartDTO cartDTO) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        Cart cartWithUpdatedData = cartMapper.cartDTOToCart(cartDTO);

        cart.setCartNumber(cartWithUpdatedData.getCartNumber());
        cart.setStatus(cartWithUpdatedData.getStatus());
        cart.setCustomer(cartWithUpdatedData.getCustomer());

        cart.getItems().removeIf(item -> !cartWithUpdatedData.getItems().contains(item));

        for (Item item : cartWithUpdatedData.getItems()) {
            cart.getItems().add(item);
        }

        addOrdinalNumberToItem(cart);

        Cart updated = cartRepository.save(cart);

        return cartMapper.cartToCartDTO(updated);
    }

    @Override
    public List<CartDTO> getAll() {
        return cartRepository.findAll().stream()
                .map(cartMapper::cartToCartDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CartDTO getById(Long id) {
        return cartRepository.findById(id)
                .map(cartMapper::cartToCartDTO)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
    }

    @Override
    @Transactional
    public void delete(Long id) {

        if (!cartRepository.existsById(id))
            throw new CartNotFoundException("Cart not found");

        cartRepository.deleteById(id);
    }

    private UUID generateCartNumber() {
        return UUID.randomUUID();
    }

    private void addOrdinalNumberToItem(Cart cart) {
        int itemCounter = 1;
        for (Item item : cart.getItems()) {
            item.getId().setOrdinalNumber(itemCounter++);
        }
    }

}

