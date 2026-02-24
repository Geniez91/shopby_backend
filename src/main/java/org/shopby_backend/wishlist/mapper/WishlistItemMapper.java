package org.shopby_backend.wishlist.mapper;

import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.wishlist.dto.WishlistAddItemOutputDto;
import org.shopby_backend.wishlist.model.WishlistEntity;
import org.shopby_backend.wishlist.model.WishlistItemEntity;
import org.springframework.stereotype.Component;

@Component
public class WishlistItemMapper {
    public WishlistItemEntity toEntity(ArticleEntity articleEntity,WishlistEntity wishlistEntity) {
        return WishlistItemEntity
                .builder()
                .article(articleEntity)
                .wishlist(wishlistEntity)
                .build();
    }

    public WishlistAddItemOutputDto toAddDto(WishlistItemEntity savedWishlistItem){
        return new WishlistAddItemOutputDto(savedWishlistItem.getWishlist().getIdWishlist(),
                savedWishlistItem.getArticle().getIdArticle(),
                savedWishlistItem.getWishlist().getUser().getId(),
                savedWishlistItem.getWishlist().getName(),
                savedWishlistItem.getWishlist().getDescription());
    }
}
