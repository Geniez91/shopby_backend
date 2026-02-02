package org.shopby_backend.order.dto;

import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.order.model.OrderQuantity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public record OrderInputDto(String deliveryAddress, BigDecimal totalPrice, Long idUser, List<OrderQuantity> articlesQuantity) {
}
