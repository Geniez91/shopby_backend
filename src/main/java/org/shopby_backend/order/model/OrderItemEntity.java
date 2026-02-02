package org.shopby_backend.order.model;

import jakarta.persistence.*;
import lombok.*;
import org.shopby_backend.article.model.ArticleEntity;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "order_item")
public class OrderItemEntity {
    @EmbeddedId
    private OrderItemId orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idOrder")
    @JoinColumn(name = "id_order")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idArticle")
    @JoinColumn(name = "id_article")
    private ArticleEntity article;

    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;
}
