package org.shopby_backend.users.dto;

import jakarta.validation.constraints.NotBlank;

public record UserResetDto(@NotBlank String email) {
}
