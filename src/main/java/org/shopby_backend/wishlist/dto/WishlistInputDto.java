package org.shopby_backend.wishlist.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WishlistInputDto(@NotNull Long userId, @NotBlank String name, @NotBlank String description) {
}
