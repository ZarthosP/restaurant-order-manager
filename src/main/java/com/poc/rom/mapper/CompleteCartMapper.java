package com.poc.rom.mapper;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.CartItem;
import com.poc.rom.entity.MenuItem;
import com.poc.rom.repository.MenuItemRepository;
import com.poc.rom.resource.CompleteCartDto;
import com.poc.rom.service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class CompleteCartMapper {

    private ModelMapper mapper;

    private MenuItemRepository menuItemRepository;

    private CartService cartService;

    public CompleteCartMapper(ModelMapper mapper, MenuItemRepository menuItemRepository, CartService cartService) {
        this.mapper = mapper;
        this.menuItemRepository = menuItemRepository;
        this.cartService = cartService;
    }

    public Cart map(CompleteCartDto resource) {
        return mapper.map(resource, Cart.class);
    }

    public CompleteCartDto map(Cart entity) {
        cartService.addMissingItemsToCart(entity);
        CompleteCartDto completeCartDto = mapper.map(entity, CompleteCartDto.class);
        return completeCartDto;
    }
}
