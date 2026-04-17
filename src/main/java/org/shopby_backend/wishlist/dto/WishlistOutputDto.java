package org.shopby_backend.wishlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represente une liste d'envie affiché coté client")
public record WishlistOutputDto(Long idWishlist,Long userId,String name,String description,Long version,String img) {
}
