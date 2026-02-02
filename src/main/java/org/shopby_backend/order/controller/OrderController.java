package org.shopby_backend.order.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.exception.order.OrderCreateException;
import org.shopby_backend.order.dto.OrderGetByUserIdDto;
import org.shopby_backend.order.dto.OrderInputDto;
import org.shopby_backend.order.dto.OrderOutputDto;
import org.shopby_backend.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class OrderController {
    private OrderService orderService;

    @PostMapping("/order")
    public OrderOutputDto addNewOrder(@RequestBody OrderInputDto orderInputDto) {
        return orderService.addNewOrder(orderInputDto);
    }

    @DeleteMapping("/order/{orderId}")
    public OrderOutputDto deleteOrder(@PathVariable Long orderId) {
        return orderService.deleteOrder(orderId);
    }

    @GetMapping("/order/{orderId}")
    public OrderOutputDto getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping("/order")
    public List<OrderOutputDto> getOrdersByUserId(@RequestBody OrderGetByUserIdDto orderGetByUserIdDto) {
        return orderService.getOrdersByUserId(orderGetByUserIdDto);
    }


}
