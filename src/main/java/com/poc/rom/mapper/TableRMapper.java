package com.poc.rom.mapper;

import com.poc.rom.entity.TableR;
import com.poc.rom.resource.TableRDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TableRMapper {

    private ModelMapper mapper;

    public TableRMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public TableR map(TableRDto resource) {
        return mapper.map(resource, TableR.class);
    }

    public TableRDto map(TableR entity) {
        return mapper.map(entity, TableRDto.class);
    }
}
