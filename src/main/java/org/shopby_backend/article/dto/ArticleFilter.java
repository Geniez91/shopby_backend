package org.shopby_backend.article.dto;

import java.math.BigDecimal;
import java.util.Date;

public record ArticleFilter(BigDecimal minPrice, BigDecimal maxPrice, Long averageRating, Long brandId,Long typeArticleId,String name) {
}
