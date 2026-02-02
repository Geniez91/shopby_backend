package org.shopby_backend.order.dto;

import java.util.List;

public record OrderOutputDto(Long idOrder, java.time.LocalDate dateOrder, String deliveryAddress, Number totalPrice, java.time.LocalDate dateDelivery, Long idUser,
                             List<OrderItemOutputDto> orderItemOutputDtos) {
}
