package org.shopby_backend.wishlist.model;

import jakarta.persistence.*;
import lombok.*;
import org.shopby_backend.article.model.ArticleEntity;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "wishlist_item")
public class WishlistItemEntity {
    @EmbeddedId
    private WishListItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("wishlistId")
    @JoinColumn(name = "wishlist_id")
    private WishlistEntity wishlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("articleId")
    @JoinColumn(name = "article_id")
    private ArticleEntity article;

    private LocalDateTime addedAt;
}
