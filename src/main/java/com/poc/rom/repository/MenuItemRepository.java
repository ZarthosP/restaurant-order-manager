package com.poc.rom.repository;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.MenuItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuItemRepository extends CrudRepository<MenuItem, Long> {
    List<MenuItem> findAll();
}
