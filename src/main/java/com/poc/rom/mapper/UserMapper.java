package com.poc.rom.mapper;

import com.poc.rom.entity.User;
import com.poc.rom.resource.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private ModelMapper mapper;

    public UserMapper(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public User map(UserDto resource) {
        return mapper.map(resource, User.class);
    }

    public UserDto map(User entity) {
        return mapper.map(entity, UserDto.class);
    }
}
