package org.shopby_backend.order.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.shopby_backend.article.model.ArticleEntity;

@Getter
@Setter
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class OrderQuantity {
    private Long idArticle;
    private Integer quantity;
}
