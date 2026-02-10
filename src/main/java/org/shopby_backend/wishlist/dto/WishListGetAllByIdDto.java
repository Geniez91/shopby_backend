package org.shopby_backend.wishlist.dto;

import jakarta.validation.constraints.NotNull;

public record WishListGetAllByIdDto(@NotNull Long userId) {
}
