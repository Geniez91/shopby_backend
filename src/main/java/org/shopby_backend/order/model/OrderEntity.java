package org.shopby_backend.order.model;

import jakarta.persistence.*;
import lombok.*;
import org.shopby_backend.status.model.StatusEntity;
import org.shopby_backend.users.model.UsersEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long idOrder;

    @Column(name = "date_order")
    private LocalDate dateOrder;

    @Column(name = "delivery_address")
    private String deliveryAdress;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "date_delivery")
    private LocalDate dateDelivery;

    @ManyToOne()
    @JoinColumn(name = "id_status")
    private StatusEntity status;

    @ManyToOne()
    @JoinColumn(name = "id_user")
    private UsersEntity user;

}
