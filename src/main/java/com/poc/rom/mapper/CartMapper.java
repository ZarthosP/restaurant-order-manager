package com.poc.rom.mapper;

import com.poc.rom.entity.Cart;
import com.poc.rom.resource.CartDto;
import org.hibernate.annotations.Comment;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CartMapper{

    private ModelMapper mapper;

    public CartMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public Cart map(CartDto resource) {
        return mapper.map(resource, Cart.class);
    }

    public CartDto map(Cart entity) {
        return mapper.map(entity, CartDto.class);
    }
}
