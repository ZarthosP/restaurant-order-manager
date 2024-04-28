package com.poc.rom.resource;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("tableR")
public class TableRDto {

    private Long id;
    private int number;
    private CartDto cart;

}
