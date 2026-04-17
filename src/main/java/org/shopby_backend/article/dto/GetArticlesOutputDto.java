package org.shopby_backend.article.dto;

import org.shopby_backend.articlePhoto.dto.ArticlePhotoOutputDto;

import java.util.Date;
import java.util.List;

public record GetArticlesOutputDto(Long idArticle, String nomArticle, String descriptionArticle, Number price, String brandName, String typeLibelle,
                                   Date creationDate, Long version, Double averageRating, Long ratingCount,
                                   String coverUrl) {
}
