package org.shopby_backend.article.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AddArticleInputDto(@NotBlank String nameArticle, @NotBlank String descriptionArticle, @NotNull BigDecimal price, @NotNull Long idBrand, @NotNull Long idType) {
}
