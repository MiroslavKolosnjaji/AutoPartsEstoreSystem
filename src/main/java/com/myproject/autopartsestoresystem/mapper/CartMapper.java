package com.myproject.autopartsestoresystem.mapper;

import com.myproject.autopartsestoresystem.dto.CartDTO;
import com.myproject.autopartsestoresystem.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author Miroslav Kološnjaji
 */
@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "items", source = "cart.items")
    CartDTO cartToCartDTO(Cart cart);

    @Mapping(target = "items", source = "items")
    Cart cartDTOToCart(CartDTO cartDTO);
}
