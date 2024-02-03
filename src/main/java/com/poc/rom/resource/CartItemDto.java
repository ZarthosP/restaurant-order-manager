package com.poc.rom.resource;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.poc.rom.entity.MenuItem;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonRootName("cartItem")
public class CartItemDto implements Serializable {

    private Long id;
    private MenuItemDto menuItem;
    private int quantity;
}
