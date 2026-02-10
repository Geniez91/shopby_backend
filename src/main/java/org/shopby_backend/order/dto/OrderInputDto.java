package org.shopby_backend.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.shopby_backend.order.model.OrderQuantity;

import java.math.BigDecimal;
import java.util.List;

public record OrderInputDto(@NotBlank String deliveryAddress, @NotNull BigDecimal totalPrice, @NotNull Long idUser, @NotNull List<OrderQuantity> articlesQuantity) {
}
