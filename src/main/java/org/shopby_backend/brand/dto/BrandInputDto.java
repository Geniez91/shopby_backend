package org.shopby_backend.brand.dto;

import jakarta.validation.constraints.NotBlank;

public record BrandInputDto(@NotBlank String libelle) {
}
