package org.shopby_backend.wishlist.persistence;

import org.shopby_backend.wishlist.model.WishListItemId;
import org.shopby_backend.wishlist.model.WishlistEntity;
import org.shopby_backend.wishlist.model.WishlistItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItemEntity, WishListItemId> {
    Boolean existsByWishlist_IdWishlistAndArticle_IdArticle(Long wishlistId, Long articleId);
    Optional<WishlistItemEntity> findByWishlist_idWishlistAndArticle_idArticle(Long wishlistId, Long articleId);
    Optional<List<WishlistItemEntity>> findByWishlist_idWishlist(Long wishlistId);
}
