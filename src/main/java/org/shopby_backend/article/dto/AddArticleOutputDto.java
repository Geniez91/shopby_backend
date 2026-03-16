package org.shopby_backend.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(description = "Represente l'article créer affiché coté client")
public record AddArticleOutputDto(Long idArticle, String nomArticle, String descriptionArticle, Number price, String brandName, String typeLibelle,
                                  Date creationDate, Long version,Double averageRating,Long ratingCount) {
}
