package com.poc.rom.mapper;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.CartItem;
import com.poc.rom.resource.CartDto;
import com.poc.rom.resource.CartItemDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    private ModelMapper mapper;

    public CartItemMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public CartItem map(CartItemDto resource) {

        CartItem cartItem = mapper.map(resource, CartItem.class);

        return cartItem;
    }

    public CartItemDto map(CartItem entity) {
        return mapper.map(entity, CartItemDto.class);
    }
}
