package com.poc.rom.controller;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.CartItem;
import com.poc.rom.mapper.CartItemMapper;
import com.poc.rom.mapper.CartMapper;
import com.poc.rom.repository.CartItemRepository;
import com.poc.rom.repository.CartRepository;
import com.poc.rom.resource.CartDto;
import com.poc.rom.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("cart")
@CrossOrigin
public class CartController {

    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private CartItemMapper cartItemMapper;
    private CartItemRepository cartItemRepository;

    private CartService cartService;

    public CartController(CartRepository cartRepository, CartMapper cartMapper, CartItemMapper cartItemMapper, CartItemRepository cartItemRepository, CartService cartService) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
    }

    @GetMapping
    public List<Cart> findAll() {
        List<Cart> all = cartRepository.findAll();
        return all;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> findById(@PathVariable Long id) {
        Optional<Cart> byId = cartRepository.findById(id);
        if (byId.isPresent()) {
            return new ResponseEntity<>(cartMapper.map(byId.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public Cart updateCart(@PathVariable long id, @RequestBody CartDto cartDto) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (cartOptional.isPresent()) {
            Cart cart = cartService.updateCart(cartOptional.get(), cartDto);
            return cart;
        }
        return null;
    }
}
