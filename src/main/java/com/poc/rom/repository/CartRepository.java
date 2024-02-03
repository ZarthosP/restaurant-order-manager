package com.poc.rom.repository;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.MenuItem;
import com.poc.rom.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartRepository extends CrudRepository<Cart, Long> {

    List<Cart> findAll();
}
