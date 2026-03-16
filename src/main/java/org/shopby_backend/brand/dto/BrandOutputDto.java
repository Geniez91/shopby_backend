package org.shopby_backend.brand.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represente la marque affiché coté client")
public record BrandOutputDto(Long id, String libelle) {
}
