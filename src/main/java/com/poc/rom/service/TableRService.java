package com.poc.rom.service;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.CartItem;
import com.poc.rom.entity.MenuItem;
import com.poc.rom.entity.TableR;
import com.poc.rom.mapper.CartItemMapper;
import com.poc.rom.mapper.TableRMapper;
import com.poc.rom.repository.CartItemRepository;
import com.poc.rom.repository.CartRepository;
import com.poc.rom.repository.MenuItemRepository;
import com.poc.rom.repository.TableRRepository;
import com.poc.rom.resource.CartDto;
import com.poc.rom.resource.CartItemDto;
import com.poc.rom.resource.CompleteCartDto;
import com.poc.rom.resource.TableRDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TableRService {


    private TableRRepository tableRRepository;
    private TableRMapper mapper;

    public TableRService(TableRRepository tableRRepository, TableRMapper mapper) {
        this.tableRRepository = tableRRepository;
        this.mapper = mapper;
    }

    public List<TableR> getAllTables() {
        return tableRRepository.findAll();
    }

    public List<TableR> findOpenedTables() {
        return tableRRepository.findOpenedTables();
    }
}
