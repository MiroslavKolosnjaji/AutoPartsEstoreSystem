package com.myproject.autopartsestoresystem.controller;

import com.myproject.autopartsestoresystem.dto.CardDTO;
import com.myproject.autopartsestoresystem.dto.CartDTO;
import com.myproject.autopartsestoresystem.exception.controller.EntityNotFoundException;
import com.myproject.autopartsestoresystem.exception.service.CartNotFoundException;
import com.myproject.autopartsestoresystem.model.Cart;
import com.myproject.autopartsestoresystem.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Miroslav Kološnjaji
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    public static final String CART_URI = "/api/cart";
    public static final String CART_ID = "/{cartId}";
    public static final String CART_URI_WITH_ID = CART_URI + CART_ID;

    private final CartService cartService;

    @PostMapping()
    public ResponseEntity<CartDTO> addCart(@Validated @RequestBody CartDTO cartDTO) {

        CartDTO saved = cartService.save(cartDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CART_URI + "/" + saved.getId());

        return new ResponseEntity<>(saved, headers, HttpStatus.CREATED);

    }

    @PutMapping(CART_ID)
    public ResponseEntity<CartDTO> updateCart(@PathVariable("cartId") Long cartId, @Validated @RequestBody CartDTO cartDTO) {

        try {

            CartDTO updated = cartService.update(cartId, cartDTO);
            return new ResponseEntity<>(updated, HttpStatus.NO_CONTENT);

        } catch (CartNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }


    @GetMapping()
    public ResponseEntity<List<CartDTO>> getCarts() {

        List<CartDTO> cartDTOList = cartService.getAll();

        if (cartDTOList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(cartDTOList, HttpStatus.OK);
    }

    @GetMapping(CART_ID)
    public ResponseEntity<CartDTO> getCart(@PathVariable("cartId") Long cartId) {

        try {

            CartDTO cartDTO = cartService.getById(cartId);
            return new ResponseEntity<>(cartDTO, HttpStatus.OK);
        } catch (CartNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @DeleteMapping(CART_ID)
    public ResponseEntity<Void> deleteCart(@PathVariable("cartId") Long cartId) {

        try {
            cartService.delete(cartId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (CartNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        }
    }


}
