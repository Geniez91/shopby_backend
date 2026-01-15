package org.shopby_backend.article.dto;

public record AddArticleInputDto(String nameArticle,String descriptionArticle,Number price,Long idBrand,Long idType) {
}
