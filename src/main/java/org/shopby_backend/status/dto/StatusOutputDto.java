package org.shopby_backend.status.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Représente un status affiché coté client")
public record StatusOutputDto(Long idStatus, String libelle ) {
}
