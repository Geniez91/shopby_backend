package org.shopby_backend.status.dto;

import jakarta.validation.constraints.NotBlank;

public record StatusInputDto(@NotBlank String libelle) {
}
