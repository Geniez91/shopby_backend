package org.shopby_backend.order.tools;

import org.shopby_backend.order.dto.OrderItemOutputDto;
import org.shopby_backend.order.dto.OrderOutputDto;
import org.shopby_backend.order.model.OrderEntity;
import org.shopby_backend.order.model.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderTools {
    public OrderOutputDto getOrderOutputDto(OrderEntity order, List<OrderItemEntity> items) {
        List<OrderItemOutputDto> itemDtos = items.stream()
                .map(i -> new OrderItemOutputDto(
                        i.getArticle().getIdArticle(),
                        i.getArticle().getName(),
                        i.getQuantity(),
                        i.getUnitPrice()
                ))
                .toList();

        return new OrderOutputDto(
                order.getIdOrder(),
                order.getDateOrder(),
                order.getDeliveryAdress(),
                order.getTotalPrice(),
                order.getDateDelivery(),
                order.getUser().getId(),
                itemDtos
        );
    }
}
