package com.poc.rom.service;

import com.poc.rom.entity.CartItem;
import com.poc.rom.entity.TableR;
import com.poc.rom.mapper.CartItemMapper;
import com.poc.rom.mapper.TableRMapper;
import com.poc.rom.repository.CartItemRepository;
import com.poc.rom.repository.TableRRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class CartItemService {


    private CartItemRepository cartItemRepository;
    private CartItemMapper mapper;

    public CartItemService(CartItemRepository cartItemRepository, CartItemMapper mapper) {
        this.cartItemRepository = cartItemRepository;
        this.mapper = mapper;
    }

    public CartItem setCartItemToReady(CartItem cartItem) {
        cartItem.setReady(cartItem.getReady() + cartItem.getConfirmed());
        cartItem.setConfirmed(0);
        cartItem.setQuantity(cartItem.getPreSelected() + cartItem.getConfirmed() + cartItem.getReady());
        return cartItemRepository.save(cartItem);
    }
}
