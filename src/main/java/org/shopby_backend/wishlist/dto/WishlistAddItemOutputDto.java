package org.shopby_backend.wishlist.dto;

public record WishlistAddItemOutputDto(Long idWishlist, Long idArticle, Long userId, String name, String description) {
}
