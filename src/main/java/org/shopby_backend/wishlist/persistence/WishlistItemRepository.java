package org.shopby_backend.wishlist.persistence;

import org.shopby_backend.wishlist.model.WishListItemId;
import org.shopby_backend.wishlist.model.WishlistEntity;
import org.shopby_backend.wishlist.model.WishlistItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishlistItemRepository extends JpaRepository<WishlistItemEntity, WishListItemId> {
    WishlistItemEntity findByWishlist_idWishlistAndArticle_idArticle(Long wishlistId,Long articleId);
    List<WishlistItemEntity> findByWishlist_idWishlist(Long wishlistId);
}
