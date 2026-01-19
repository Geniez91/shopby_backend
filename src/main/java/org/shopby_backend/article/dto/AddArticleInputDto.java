package org.shopby_backend.article.dto;

import java.math.BigDecimal;

public record AddArticleInputDto(String nameArticle, String descriptionArticle, BigDecimal price, Long idBrand, Long idType) {
}
