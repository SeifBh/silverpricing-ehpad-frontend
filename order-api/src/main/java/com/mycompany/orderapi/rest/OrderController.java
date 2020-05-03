package com.mycompany.orderapi.rest;

import com.mycompany.orderapi.mapper.OrderMapper;
import com.mycompany.orderapi.model.Order;
import com.mycompany.orderapi.model.User;
import com.mycompany.orderapi.rest.dto.CreateOrderRequest;
import com.mycompany.orderapi.rest.dto.OrderDto;
import com.mycompany.orderapi.security.CustomUserDetails;
import com.mycompany.orderapi.service.OrderService;
import com.mycompany.orderapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    public OrderController(UserService userService, OrderService orderService, OrderMapper orderMapper) {
        this.userService = userService;
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public List<OrderDto> getOrders(@RequestParam(value = "text", required = false) String text) {
        List<Order> orders = (text == null) ? orderService.getOrders() : orderService.getOrderContainingText(text);
        return orders.stream()
                .map(order -> orderMapper.toOrderDto(order))
                .collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderDto createOrder(@AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CreateOrderRequest createOrderRequest) {
        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
        Order order = orderMapper.toOrder(createOrderRequest);
        order.setId(UUID.randomUUID().toString());
        order.setUser(user);
        return orderMapper.toOrderDto(orderService.saveOrder(order));
    }

    @DeleteMapping("/{id}")
    public OrderDto deleteOrders(@PathVariable UUID id) {
        Order order = orderService.validateAndGetOrder(id.toString());
        orderService.deleteOrder(order);
        return orderMapper.toOrderDto(order);
    }

}
