package org.shopby_backend.article.dto;

import java.util.Date;

public record AddArticleOutputDto(Long idArticle, String nomArticle, String descriptionArticle, Number price, String brandName, String typeLibelle,
                                  Date creationDate) {
}
