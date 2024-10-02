package com.poc.rom.repository;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.CartItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    List<CartItem> findAll();

    @Query("SELECT c FROM CartItem c where c.menuItem.id = :menuItemId and c.cart.id = :cartId")
    Optional<CartItem> findByMenuItemIdAndAndCartId(Long menuItemId, Long cartId);
}
