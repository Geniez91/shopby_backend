package org.shopby_backend.order.persistence;

import org.shopby_backend.order.model.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {
    Optional<OrderEntity> findByDateOrderAndStatus_libelle(LocalDate dateOrder, String libelle);
    Page<OrderEntity> findByUser_Id(Long id, Pageable pageable);
}
