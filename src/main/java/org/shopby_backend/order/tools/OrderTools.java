package org.shopby_backend.order.tools;

import lombok.AllArgsConstructor;
import org.shopby_backend.order.dto.OrderItemOutputDto;
import org.shopby_backend.order.dto.OrderOutputDto;
import org.shopby_backend.order.mapper.OrderItemMapper;
import org.shopby_backend.order.mapper.OrderMapper;
import org.shopby_backend.order.model.OrderEntity;
import org.shopby_backend.order.model.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class OrderTools {
    private OrderMapper orderMapper;
    private OrderItemMapper orderItemMapper;

    public OrderOutputDto getOrderOutputDto(OrderEntity order, List<OrderItemEntity> items) {
        List<OrderItemOutputDto> itemDto = items.stream()
                .map(i -> orderItemMapper.toDto(i))
                .toList();
        return orderMapper.toDto(order, itemDto);
    }
}
