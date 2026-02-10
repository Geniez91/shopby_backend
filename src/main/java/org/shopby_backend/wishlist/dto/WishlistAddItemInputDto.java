package org.shopby_backend.wishlist.dto;

import jakarta.validation.constraints.NotNull;

public record WishlistAddItemInputDto(@NotNull Long idArticle) {
}
