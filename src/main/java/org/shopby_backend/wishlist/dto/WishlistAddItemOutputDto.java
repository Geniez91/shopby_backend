package org.shopby_backend.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represente l'article et la liste d'envie affiché coté client")
public record WishlistAddItemOutputDto(Long idWishlist, Long idArticle, Long userId, String name, String description) {
}
