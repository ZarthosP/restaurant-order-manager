package com.poc.rom.controller;

import com.poc.rom.entity.MenuItem;
import com.poc.rom.repository.CartRepository;
import com.poc.rom.repository.MenuItemRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("menuItem")
@CrossOrigin
public class MenuItemController {

    private MenuItemRepository menuItemRepository;

    public MenuItemController(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping
    public List<MenuItem> test() {
        return menuItemRepository.findAll();
    }
}
