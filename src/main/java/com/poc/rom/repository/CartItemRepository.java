package com.poc.rom.repository;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.CartItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    List<CartItem> findAll();
}
