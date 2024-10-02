package com.poc.rom.controller;

import com.poc.rom.entity.*;
import com.poc.rom.enums.MenuItemType;
import com.poc.rom.mapper.CartItemMapper;
import com.poc.rom.mapper.CartMapper;
import com.poc.rom.mapper.CompleteCartMapper;
import com.poc.rom.mapper.PaymentBundleMapper;
import com.poc.rom.repository.*;
import com.poc.rom.resource.*;
import com.poc.rom.service.CartItemService;
import com.poc.rom.service.CartService;
import com.poc.rom.service.TableRService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("cart")
@MessageMapping("cartWS")
@CrossOrigin
public class CartController {

    private final TableRRepository tableRRepository;
    private final MenuItemRepository menuItemRepository;
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private CompleteCartMapper completeCartMapper;
    private CartItemMapper cartItemMapper;
    private CartItemRepository cartItemRepository;
    private TableRService tableRService;
    private SimpMessagingTemplate messagingTemplate;
    private CartItemService cartItemService;
    private TableRController tableRController;
    private PaymentBundleMapper paymentBundleMapper;
    private PaymentBundleRepository paymentBundleRepository;


    private CartService cartService;

    public CartController(CartRepository cartRepository, CartMapper cartMapper, CompleteCartMapper completeCartMapper, CartItemMapper cartItemMapper, CartItemRepository cartItemRepository, TableRService tableRService, SimpMessagingTemplate messagingTemplate, CartItemService cartItemService, TableRController tableRController, CartService cartService, TableRRepository tableRRepository, PaymentBundleMapper paymentBundleMapper, MenuItemRepository menuItemRepository, PaymentBundleRepository paymentBundleRepository) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.completeCartMapper = completeCartMapper;
        this.cartItemMapper = cartItemMapper;
        this.cartItemRepository = cartItemRepository;
        this.tableRService = tableRService;
        this.messagingTemplate = messagingTemplate;
        this.cartItemService = cartItemService;
        this.tableRController = tableRController;
        this.cartService = cartService;
        this.tableRRepository = tableRRepository;
        this.paymentBundleMapper = paymentBundleMapper;
        this.menuItemRepository = menuItemRepository;
        this.paymentBundleRepository = paymentBundleRepository;
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
        List<TableR> allTables = tableRService.findOpenedTables();
        return allTables;
    }

    @GetMapping("/cartItem/setReady/{id}")
    public CartItem setCartItemReady(@PathVariable Long id) {
        Optional<CartItem> byId = cartItemRepository.findById(id);
        if (byId.isPresent()) {
            CartItem cartItem = cartItemService.setCartItemToReady(byId.get());

            TableR table = tableRRepository.findByCart(cartItem.getCart());
            List<TableRDto> tableRDtos = tableRController.addNotification(table.getId(), null, cartItem.getMenuItem().getMenuItemType() == MenuItemType.MEAL || cartItem.getMenuItem().getMenuItemType() == MenuItemType.DESSERT ? true : null, cartItem.getMenuItem().getMenuItemType() == MenuItemType.DRINK ? true : null);

            List<TableR> allTables = getKitchenOrdersSocket();
            messagingTemplate.convertAndSend("/topic/kitchen", allTables);


            CartRequest cartRequest = new CartRequest();
            cartRequest.setId(Math.toIntExact(table.getCart().getId()));
            cartRequest.setCompleteCartDto(completeCartMapper.map(table.getCart()));

            CompleteCartDto completeCartSocket = getCompleteCartSocket(cartRequest);
            messagingTemplate.convertAndSend("/topic/cart", completeCartSocket);

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

    @PostMapping("/createPaymentBundle")
    public PaymentBundle addTable(@RequestBody PaymentBundleDto paymentBundleDto) {
        ArrayList<PrePaymentCartItem> prePaymentCartItems = new ArrayList<>();
        if (!paymentBundleDto.getPrePaymentCartItems().isEmpty()) {
            paymentBundleDto.getPrePaymentCartItems().forEach(prePaymentCartItemDto -> {
                Optional<MenuItem> menuItem = menuItemRepository.findById(prePaymentCartItemDto.getMenuItem().getId());
                if (menuItem.isPresent()) {
                    PrePaymentCartItem paymentCartItem = PrePaymentCartItem.builder()
                            .quantity(prePaymentCartItemDto.getQuantity())
                            .menuItem(menuItem.get())
                            .build();
                    prePaymentCartItems.add(paymentCartItem);
                }
            });
        }
        PaymentBundle paymentBundle = new PaymentBundle();
        paymentBundle.setName(paymentBundleDto.getName());
        paymentBundle.setCartId(paymentBundleDto.getCartId());
        paymentBundle.setTableId(paymentBundleDto.getTableId());

        for (PrePaymentCartItem prePaymentCartItem : prePaymentCartItems) {
            prePaymentCartItem.setPaymentBundle(paymentBundle);
        }

        paymentBundle.setPrePaymentCartItems(prePaymentCartItems);
        PaymentBundle save = paymentBundleRepository.save(paymentBundle);

        List<PaymentBundle> bundles = getBundleList(save.getTableId());
        messagingTemplate.convertAndSend("/topic/bundleList", bundles);
        return save;
    }

    @GetMapping("/getAllPaymentBundleForTable/{id}")
    public List<PaymentBundle> getAllPaymentBundles(@PathVariable long id) {
        List<PaymentBundle> byTableId = paymentBundleRepository.findByTableId(id);
        return byTableId;
    }

    @GetMapping("/validateBundle/{id}")
    public String validateBundle(@PathVariable long id) {
        Optional<PaymentBundle> bundle = paymentBundleRepository.findById(id);
        if (bundle.isPresent()) {
            Optional<Cart> cart = cartRepository.findById(bundle.get().getCartId());
            if (cart.isPresent()) {
                cart.get().getCartItems().forEach(cartItem -> {
                    Optional<PrePaymentCartItem> first = bundle.get().getPrePaymentCartItems().stream().filter(prePaymentCartItem -> prePaymentCartItem.getMenuItem() == cartItem.getMenuItem()).findFirst();
                    if (first.isPresent()) {
                        cartItem.setReady(cartItem.getReady() - first.get().getQuantity());
                        cartItem.setPayed(cartItem.getPayed() + first.get().getQuantity());
                        cartItemRepository.save(cartItem);
                    }
                });
                CartRequest cartRequest = new CartRequest();
                cartRequest.setId(Math.toIntExact(cart.get().getId()));
                cartRequest.setCompleteCartDto(completeCartMapper.map(cart.get()));

                CompleteCartDto completeCartSocket = getCompleteCartSocket(cartRequest);
                messagingTemplate.convertAndSend("/topic/cart", completeCartSocket);
            }
            Long tableId = bundle.get().getTableId();
            paymentBundleRepository.delete(bundle.get());
            List<PaymentBundle> bundles = getBundleList(tableId);
            messagingTemplate.convertAndSend("/topic/bundleList", bundles);
        }
        return "ok";
    }


    @MessageMapping("/getBundleList/{id}")
    @SendTo("/topic/bundleList")
    public List<PaymentBundle> getBundleList(@DestinationVariable Long id) {
        List<PaymentBundle> byTableId = paymentBundleRepository.findByTableId(id);
        return byTableId;
    }

}
