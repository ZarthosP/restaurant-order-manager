package com.poc.rom.controller;

import com.poc.rom.entity.Cart;
import com.poc.rom.entity.CartItem;
import com.poc.rom.entity.TableR;
import com.poc.rom.enums.MenuItemType;
import com.poc.rom.mapper.CartItemMapper;
import com.poc.rom.mapper.CartMapper;
import com.poc.rom.mapper.CompleteCartMapper;
import com.poc.rom.repository.CartItemRepository;
import com.poc.rom.repository.CartRepository;
import com.poc.rom.resource.CartDto;
import com.poc.rom.resource.CartRequest;
import com.poc.rom.resource.CompleteCartDto;
import com.poc.rom.service.CartItemService;
import com.poc.rom.service.CartService;
import com.poc.rom.service.TableRService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("cart")
@MessageMapping("cartWS")
@CrossOrigin
public class CartController {

    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private CompleteCartMapper completeCartMapper;
    private CartItemMapper cartItemMapper;
    private CartItemRepository cartItemRepository;
    private TableRService tableRService;
    private SimpMessagingTemplate messagingTemplate;
    private CartItemService cartItemService;

    private CartService cartService;

    public CartController(CartRepository cartRepository, CartMapper cartMapper, CompleteCartMapper completeCartMapper, CartItemMapper cartItemMapper, CartItemRepository cartItemRepository, TableRService tableRService, SimpMessagingTemplate messagingTemplate, CartItemService cartItemService, CartService cartService) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.completeCartMapper = completeCartMapper;
        this.cartItemMapper = cartItemMapper;
        this.cartItemRepository = cartItemRepository;
        this.tableRService = tableRService;
        this.messagingTemplate = messagingTemplate;
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }

    @GetMapping("/getCompleteCart/{id}")
    public ResponseEntity<CompleteCartDto> getCompleteCart(@PathVariable Long id) {
        Optional<Cart> byId = cartRepository.findById(id);
        if (byId.isPresent()) {
            return new ResponseEntity<>(completeCartMapper.map(byId.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @MessageMapping("/completeCart")
    @SendTo("/topic/cart")
    public CompleteCartDto getCompleteCartSocket(@Payload CartRequest request) {
        int id = request.getId();
        CompleteCartDto completeCartDto = request.getCompleteCartDto();
        Optional<Cart> cart = cartRepository.findById((long) id);
        if (cart.isPresent()) {
            if (completeCartDto != null) {
                cartService.refreshCartItems(cart.get(), completeCartDto);
            }
            List<TableR> allTables = getKitchenOrdersSocket();
            messagingTemplate.convertAndSend("/topic/kitchen", allTables);
            return completeCartMapper.map(cart.get());
        }
        return null;
    }

    @MessageMapping("/kitchenOrders")
    @SendTo("/topic/kitchen")
    public List<TableR> getKitchenOrdersSocket() {
        List<TableR> allTables = tableRService.getAllTables();
        return allTables;
    }

    @GetMapping("/cartItem/setReady/{id}")
    public CartItem setCartItemReady(@PathVariable Long id) {
        Optional<CartItem> byId = cartItemRepository.findById(id);
        if (byId.isPresent()) {
            CartItem cartItem = cartItemService.setCartItemToReady(byId.get());
            List<TableR> allTables = getKitchenOrdersSocket();
            messagingTemplate.convertAndSend("/topic/kitchen", allTables);
            return cartItem;
        }
        return null;
    }

    @GetMapping
    public List<Cart> findAll() {
        List<Cart> all = cartRepository.findAll();
        return all;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> findById(@PathVariable Long id) {
        Optional<Cart> byId = cartRepository.findById(id);
        if (byId.isPresent()) {
            return new ResponseEntity<>(cartMapper.map(byId.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public Cart updateCart(@PathVariable long id, @RequestBody CartDto cartDto) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (cartOptional.isPresent()) {
            Cart cart = cartService.updateCart(cartOptional.get(), cartDto);
            return cart;
        }
        return null;
    }

    @PutMapping("/validate/{id}")
    public Cart validateOrder(@PathVariable long id) {
        Optional<Cart> cartOptional = cartRepository.findById(id);
        if (cartOptional.isPresent()) {
            Cart cart = cartService.validateOrder(cartOptional.get());
            return cart;
        }
        return null;
    }

    @GetMapping("/kitchen/orders")
    public List<CartItem> getOrders() {
        List<CartItem> all = cartItemRepository.findAll();
        List<CartItem> cartItems = all.stream().filter(cartItem -> cartItem.getConfirmed() > 0 && cartItem.getMenuItem().getMenuItemType() != MenuItemType.DRINK).toList();
        return cartItems;
    }

    @GetMapping("/bar/orders")
    public List<CartItem> getBarOrders() {
        List<CartItem> all = cartItemRepository.findAll();
        List<CartItem> cartItems = all.stream().filter(cartItem -> cartItem.getConfirmed() > 0 && cartItem.getMenuItem().getMenuItemType() == MenuItemType.DRINK).toList();
        return cartItems;
    }

    @PutMapping("/cartItem/ready/{id}")
    public List<CartItem> cartItemReady(@PathVariable long id) {
        Optional<CartItem> byId = cartItemRepository.findById(id);
        if (byId.isPresent()) {
            cartService.cartItemReady(byId.get());

            return byId.get().getMenuItem().getMenuItemType() == MenuItemType.DRINK ? getBarOrders() : getOrders();
//            List<CartItem> all = cartItemRepository.findAll();
//
//            return all.stream().filter(cartItem -> cartItem.getConfirmed() > 0).toList();
        }
        return null;
    }

    @GetMapping("/getUnpaidItems/{id}")
    public ResponseEntity<List<CartItem>> getUnpayedItems(@PathVariable long id) {
        Optional<Cart> byId = cartRepository.findById(id);
        if (byId.isPresent()) {
            List<CartItem> cartItems = byId.get().getCartItems().stream().filter(cartItem -> cartItem.getReady() > 0).toList();
            return new ResponseEntity<>(cartItems, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
