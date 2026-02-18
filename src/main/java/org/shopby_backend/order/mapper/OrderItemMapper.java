package org.shopby_backend.order.mapper;

import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.order.dto.OrderItemOutputDto;
import org.shopby_backend.order.model.OrderEntity;
import org.shopby_backend.order.model.OrderItemEntity;
import org.shopby_backend.order.model.OrderItemId;
import org.shopby_backend.order.model.OrderQuantity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItemEntity toEntity(OrderEntity orderEntity, ArticleEntity articleEntity, OrderQuantity orderQuantity) {
        return OrderItemEntity.builder()
                .orderItemId(new OrderItemId(orderEntity.getIdOrder(), articleEntity.getIdArticle()))
                .article(articleEntity)
                .order(orderEntity)
                .quantity(orderQuantity.getQuantity())
                .unitPrice(articleEntity.getPrice())
                .build();
    }

    public OrderItemOutputDto toDto(OrderItemEntity orderItemEntity) {
        return new OrderItemOutputDto(
                orderItemEntity.getArticle().getIdArticle(),
                orderItemEntity.getArticle().getName(),
                orderItemEntity.getQuantity(),
                orderItemEntity.getUnitPrice()
        );
    }}
