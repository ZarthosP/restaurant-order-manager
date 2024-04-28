package com.poc.rom.repository;

import com.poc.rom.entity.CartItem;
import com.poc.rom.entity.TableR;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TableRRepository extends CrudRepository<TableR, Long> {

    List<TableR> findAll();
}
