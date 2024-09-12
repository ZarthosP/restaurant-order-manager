package com.poc.rom.resource;

import lombok.Data;

@Data
public class CartRequest {
    private int id;
    private CompleteCartDto completeCartDto;
}

