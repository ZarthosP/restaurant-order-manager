package com.poc.rom.resource;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.poc.rom.enums.TableStatus;
import lombok.Data;

import java.util.List;

@Data
@JsonRootName("tableR")
public class TableRDto {

    private Long id;
    private int number;
    private int numberOfClients;
    private boolean clientNotification;
    private boolean kitchenNotification;
    private boolean barNotification;
    private String tableStatus;
    private CartDto cart;

}
