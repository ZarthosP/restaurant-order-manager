package com.poc.rom.controller;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.TableR;
import com.poc.rom.mapper.TableRMapper;
import com.poc.rom.repository.TableRRepository;
import com.poc.rom.resource.TableRDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("table")
@CrossOrigin
public class TableRController {

    private TableRRepository tableRRepository;
    private TableRMapper mapper;

    public TableRController(TableRRepository tableRRepository, TableRMapper mapper) {
        this.tableRRepository = tableRRepository;
        this.mapper = mapper;
    }


    @GetMapping
    public ResponseEntity<List<TableRDto>> findAll() {
        List<TableR> all = tableRRepository.findAll();
        if (!all.isEmpty()) {
            List<TableRDto> list = new ArrayList<>();
            all.forEach(tableR -> list.add(mapper.map(tableR)));
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableRDto> findById(@PathVariable Long id) {
        Optional<TableR> byId = tableRRepository.findById(id);
        if (byId.isPresent()) {
            return new ResponseEntity<>(mapper.map(byId.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
