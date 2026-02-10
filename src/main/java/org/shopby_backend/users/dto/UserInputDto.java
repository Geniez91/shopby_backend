package org.shopby_backend.users.dto;

import jakarta.validation.constraints.NotBlank;

public record UserInputDto(@NotBlank String prenom, @NotBlank String nom, @NotBlank String password, @NotBlank String email, @NotBlank String country) {
}
