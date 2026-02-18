package org.shopby_backend.order.mapper;

import org.shopby_backend.order.dto.OrderInputDto;
import org.shopby_backend.order.dto.OrderItemOutputDto;
import org.shopby_backend.order.dto.OrderOutputDto;
import org.shopby_backend.order.model.OrderEntity;
import org.shopby_backend.order.model.OrderItemEntity;
import org.shopby_backend.status.model.StatusEntity;
import org.shopby_backend.users.model.UsersEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class OrderMapper {
    public OrderEntity toEntity(OrderInputDto orderInputDto, StatusEntity statusEntity, LocalDate currentDate, LocalDate dateDelivery, UsersEntity usersEntity){
      return OrderEntity.builder()
                .deliveryAdress(orderInputDto.deliveryAddress())
                .totalPrice(orderInputDto.totalPrice())
                .status(statusEntity)
                .dateOrder(currentDate)
                .dateDelivery(dateDelivery)
                .user(usersEntity)
                .build();
    }

    public OrderOutputDto toDto(OrderEntity orderEntity, List<OrderItemOutputDto> orderItemEntityList){
      return new OrderOutputDto(
              orderEntity.getIdOrder(),
              orderEntity.getDateOrder(),
              orderEntity.getDeliveryAdress(),
              orderEntity.getTotalPrice(),
              orderEntity.getDateDelivery(),
              orderEntity.getUser().getId(),
              orderItemEntityList
      );
    }
}
