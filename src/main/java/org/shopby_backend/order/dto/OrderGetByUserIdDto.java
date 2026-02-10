package org.shopby_backend.order.dto;

import jakarta.validation.constraints.NotNull;

public record OrderGetByUserIdDto(@NotNull Long userId) {
}
