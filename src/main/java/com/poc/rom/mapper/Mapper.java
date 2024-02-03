package com.poc.rom.mapper;

import java.io.Serializable;

public interface Mapper<E, R extends Serializable> {

    E map(R resource);

    R map(E entity);

    default void map(R resource, E entity){
        //only needed for updates
    }
}
