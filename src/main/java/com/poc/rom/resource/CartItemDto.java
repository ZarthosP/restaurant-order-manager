package com.poc.rom.resource;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.poc.rom.entity.MenuItem;
import com.poc.rom.enums.OrderStatus;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonRootName("cartItem")
public class CartItemDto implements Serializable {

    private Long id;
    private MenuItemDto menuItem;
    private int quantity;
    private int preSelected;
    private int confirmed;
    private int ready;
    private int payed;
    private String orderStatus;
}
