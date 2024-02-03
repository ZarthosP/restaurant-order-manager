package com.poc.rom.mapper;

import com.poc.rom.entity.MenuItem;
import com.poc.rom.resource.MenuItemDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {

    private ModelMapper mapper;

    public MenuItemMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public MenuItem map(MenuItemDto resource) {
        return mapper.map(resource, MenuItem.class);
    }

    public MenuItemDto map(MenuItem entity) {
        return mapper.map(entity, MenuItemDto.class);
    }
}
