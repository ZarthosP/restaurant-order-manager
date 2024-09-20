package com.poc.rom.repository;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.CartItem;
import com.poc.rom.entity.TableR;
import com.poc.rom.enums.TableStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface TableRRepository extends CrudRepository<TableR, Long> {

    List<TableR> findAll();

    @Query("SELECT t FROM TableR t where t.tableStatus = 'OPEN'")
    List<TableR> findOpenedTables();

    TableR findByCart(Cart cart);
}
