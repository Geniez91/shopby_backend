package org.shopby_backend.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Represente une commande affiché coté client")
public record OrderOutputDto(Long idOrder, java.time.LocalDate dateOrder, String deliveryAddress, Number totalPrice, java.time.LocalDate dateDelivery, Long idUser,
                             List<OrderItemOutputDto> orderItemOutputDtos) {
}
