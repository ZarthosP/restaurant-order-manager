package com.poc.rom.service;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.CartItem;
import com.poc.rom.entity.MenuItem;
import com.poc.rom.mapper.CartItemMapper;
import com.poc.rom.repository.CartItemRepository;
import com.poc.rom.repository.CartRepository;
import com.poc.rom.repository.MenuItemRepository;
import com.poc.rom.resource.CartDto;
import com.poc.rom.resource.CartItemDto;
import com.poc.rom.resource.CompleteCartDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
public class CartService {

    private final CartItemMapper cartItemMapper;
    private MenuItemRepository menuItemRepository;
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;

    public CartService(MenuItemRepository menuItemRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, CartItemMapper cartItemMapper) {
        this.menuItemRepository = menuItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartItemMapper = cartItemMapper;
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

    public Cart addMissingItemsToCart(Cart cart) {
        List<MenuItem> completeMenu = menuItemRepository.findAll();

        completeMenu.forEach(menuItem -> {
            if (cart.getCartItems().stream().noneMatch(cartItemDto -> Objects.equals(cartItemDto.getMenuItem().getId(), menuItem.getId()))) {
                MenuItem managedMenuItem = menuItemRepository.findById(menuItem.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid MenuItem ID"));

                CartItem cartItem = CartItem.builder()
                        .menuItem(managedMenuItem)
                        .quantity(0)
                        .preSelected(0)
                        .confirmed(0)
                        .ready(0)
                        .payed(0)
                        .cart(cart)
                        .build();

                cart.getCartItems().add(cartItemRepository.save(cartItem));
            }
        });

        return cartRepository.save(cart);
    }

    public Cart refreshCartItems(Cart cart, CompleteCartDto completeCartDto) {
        cart.getCartItems().forEach(cartItem -> {
            CartItemDto cartItemById = completeCartDto.findCartItemById(cartItem.getId());
            cartItem.setPreSelected(cartItemById.getPreSelected());
            cartItem.setConfirmed(cartItemById.getConfirmed());
            cartItem.setReady(cartItemById.getReady());
            cartItem.setPayed(cartItemById.getPayed());
            cartItem.setQuantity(cartItemById.getPayed());
            cartItem.setQuantity(cartItem.getPreSelected() + cartItem.getConfirmed() + cartItem.getPayed() + cartItem.getPayed());
            cartItemRepository.save(cartItem);
        });
        return cart;
    }
}
