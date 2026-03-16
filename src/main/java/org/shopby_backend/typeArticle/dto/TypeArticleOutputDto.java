package org.shopby_backend.typeArticle.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Représente un type d'article affiché coté client")
public record TypeArticleOutputDto(Number id,String libelle,Number parentId) {
}
