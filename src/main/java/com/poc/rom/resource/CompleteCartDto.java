package com.poc.rom.resource;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonRootName("cart")
public class CompleteCartDto implements Serializable {

    private Long id;
    private String title;
    private List<CartItemDto> cartItems;

    public CartItemDto findCartItemById(Long id) {
        for (CartItemDto cartItem : this.cartItems) {
            if (cartItem.getId().equals(id)) {
                return cartItem;
            }
        }
        return null;
    }
}
