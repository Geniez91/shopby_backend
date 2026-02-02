package org.shopby_backend.order.persistence;

import org.shopby_backend.order.model.OrderItemEntity;
import org.shopby_backend.order.model.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, OrderItemId> {
   Optional<List<OrderItemEntity>> findByOrder_IdOrder(Long orderId);
}
