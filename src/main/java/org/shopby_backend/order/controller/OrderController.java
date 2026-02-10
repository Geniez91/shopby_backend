package org.shopby_backend.order.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.order.dto.OrderGetByUserIdDto;
import org.shopby_backend.order.dto.OrderInputDto;
import org.shopby_backend.order.dto.OrderOutputDto;
import org.shopby_backend.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderOutputDto addNewOrder(@RequestBody OrderInputDto orderInputDto) {
        return orderService.addNewOrder(orderInputDto);
    }


    @DeleteMapping("/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public OrderOutputDto deleteOrder(@PathVariable Long orderId) {
        return orderService.deleteOrder(orderId);
    }

    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderOutputDto getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderOutputDto> getOrdersByUserId(@RequestBody OrderGetByUserIdDto orderGetByUserIdDto) {
        return orderService.getOrdersByUserId(orderGetByUserIdDto);
    }


}
