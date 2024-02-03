package com.poc.rom.resource;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonRootName("menuItem")
public class MenuItemDto implements Serializable {

    private Long id;
    private String title;
    private String description;
    private float price;
    private String menuItemType;
}
