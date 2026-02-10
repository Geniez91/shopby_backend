package org.shopby_backend.typeArticle.dto;

import jakarta.validation.constraints.NotBlank;

public record TypeArticleDto(@NotBlank String libelle, Long parentId) {
}
