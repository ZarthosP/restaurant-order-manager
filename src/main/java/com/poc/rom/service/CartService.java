package com.poc.rom.service;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.CartItem;
import com.poc.rom.entity.MenuItem;
import com.poc.rom.enums.OrderStatus;
import com.poc.rom.repository.CartItemRepository;
import com.poc.rom.repository.CartRepository;
import com.poc.rom.repository.MenuItemRepository;
import com.poc.rom.resource.CartDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private MenuItemRepository menuItemRepository;
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;

    public CartService(MenuItemRepository menuItemRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.menuItemRepository = menuItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public Cart updateCart(Cart cart, CartDto cartDto) {
        cartDto.getCartItems().forEach(cartItemDto -> {
            if (cartItemDto.getId() == null) {
                CartItem cartItem = new CartItem();
                Optional<MenuItem> menuItem = menuItemRepository.findById(cartItemDto.getMenuItem().getId());
                cartItem.setMenuItem(menuItem.get());
                cartItem.setPreSelected(cartItemDto.getPreSelected());
//                cartItem.setQuantity(cartItemDto.getQuantity());
                cartItem.setCart(cart);
                cart.getCartItems().add(cartItem);
                cartItemRepository.save(cartItem);
            } else {
                Optional<CartItem> cartItem = cartItemRepository.findById(cartItemDto.getId());
//                cartItem.get().setQuantity(cartItemDto.getQuantity());
                cartItem.get().setPreSelected(cartItemDto.getPreSelected());
            }
        });
        return cartRepository.save(cart);
    }

    public Cart validateOrder(Cart cart) {
        cart.getCartItems().forEach(cartItem -> {
            if (cartItem.getPreSelected() > 0) {
                cartItem.setConfirmed(cartItem.getConfirmed() + cartItem.getPreSelected());
                cartItem.setPreSelected(0);
                cartItemRepository.save(cartItem);
            }
        });
        return cart;
    }

    public CartItem cartItemReady(CartItem cartItem) {
        cartItem.setReady(cartItem.getReady() + cartItem.getConfirmed());
        cartItem.setConfirmed(0);
        return cartItemRepository.save(cartItem);
    }
}
