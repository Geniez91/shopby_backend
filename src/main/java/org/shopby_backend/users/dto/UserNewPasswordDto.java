package org.shopby_backend.users.dto;

import jakarta.validation.constraints.NotBlank;

public record UserNewPasswordDto(@NotBlank String email, @NotBlank String code, @NotBlank String password) {
}
