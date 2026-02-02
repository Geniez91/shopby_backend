package org.shopby_backend.order.dto;

import java.math.BigDecimal;

public record OrderItemOutputDto(Long idArticle, String name, Integer quantity, BigDecimal unitPrice) {
}
