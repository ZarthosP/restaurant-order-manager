package com.poc.rom.controller;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.TableR;
import com.poc.rom.enums.TableStatus;
import com.poc.rom.mapper.TableRMapper;
import com.poc.rom.repository.CartRepository;
import com.poc.rom.repository.TableRRepository;
import com.poc.rom.resource.TableRDto;
import com.poc.rom.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("table")
@MessageMapping("tableWS")
@CrossOrigin
public class TableRController {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private TableRRepository tableRRepository;
    private TableRMapper mapper;
    private SimpMessagingTemplate messagingTemplate;

    public TableRController(TableRRepository tableRRepository, TableRMapper mapper, CartService cartService, CartRepository cartRepository, SimpMessagingTemplate messagingTemplate) {
        this.tableRRepository = tableRRepository;
        this.mapper = mapper;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.messagingTemplate = messagingTemplate;
    }


    @GetMapping("/findAllOpenTables")
    public List<TableRDto> findAllOpenTables() {
        List<TableR> all = tableRRepository.findOpenedTables();
        if (!all.isEmpty()) {
            List<TableRDto> list = new ArrayList<>();
            all.forEach(tableR -> list.add(mapper.map(tableR)));
            return list;
        }
        return null;
    }

    @PostMapping("/addTable")
    public List<TableRDto> addTable(@RequestBody TableRDto tableRDto) {
        Cart cart = cartRepository.save(new Cart());
        TableR map = mapper.map(tableRDto);
        map.setCart(cart);
        TableR save = tableRRepository.save(map);
        List<TableR> all = tableRRepository.findOpenedTables();
        if (!all.isEmpty()) {
            List<TableRDto> list = new ArrayList<>();
            all.forEach(tableR -> list.add(mapper.map(tableR)));
            return list;
        }
        return null;
    }

    @GetMapping("/closeTable/{id}")
    public List<TableRDto> addTable(@PathVariable Long id) {
        Optional<TableR> byId = tableRRepository.findById(id);
        if (byId.isPresent()) {
            byId.get().setTableStatus(TableStatus.CLOSE);
            tableRRepository.save(byId.get());
            List<TableR> all = tableRRepository.findOpenedTables();
            if (!all.isEmpty()) {
                List<TableRDto> list = new ArrayList<>();
                all.forEach(tableR -> list.add(mapper.map(tableR)));
                return list;
            }
        }
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableRDto> findById(@PathVariable Long id) {
        Optional<TableR> byId = tableRRepository.findById(id);
        if (byId.isPresent()) {
            return new ResponseEntity<>(mapper.map(byId.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @SendTo("/topic/alerts")
    public List<TableRDto> sendNotification(Long id, Boolean clientNotification, Boolean kitchenNotification,Boolean barNotification) {
        Optional<TableR> table = tableRRepository.findById(id);
        if (table.isPresent()) {
            table.get().setClientNotification(clientNotification == null ? table.get().isClientNotification() : clientNotification);
            table.get().setKitchenNotification(kitchenNotification == null ? table.get().isKitchenNotification() : kitchenNotification);
            table.get().setBarNotification(barNotification == null ? table.get().isBarNotification() : barNotification);
            tableRRepository.save(table.get());
            List<TableR> all = tableRRepository.findOpenedTables();
            if (!all.isEmpty()) {
                List<TableRDto> list = new ArrayList<>();
                all.forEach(tableR -> list.add(mapper.map(tableR)));
                return list;
            }
        }
        return null;
    }

    @PostMapping("/removeNotification")
    public List<TableRDto> removeNotification(@RequestBody TableRDto tableRDto) {
        List<TableRDto> tables = sendNotification(tableRDto.getId(), tableRDto.isClientNotification(), tableRDto.isKitchenNotification(), tableRDto.isBarNotification());
        messagingTemplate.convertAndSend("/topic/alerts", tables);
        return tables;
    }

    @GetMapping("/callWaiter/{id}")
    public List<TableRDto> callWaiter(@PathVariable Long id) {
        List<TableRDto> tables = sendNotification(id, true, null, null);
        messagingTemplate.convertAndSend("/topic/alerts", tables);
        return tables;
    }

    public List<TableRDto> addNotification(Long id, Boolean clientNotification, Boolean kitchenNotification,Boolean barNotification) {
        List<TableRDto> tables = sendNotification(id, clientNotification,kitchenNotification, barNotification);
        messagingTemplate.convertAndSend("/topic/alerts", tables);
        return tables;
    }
}
