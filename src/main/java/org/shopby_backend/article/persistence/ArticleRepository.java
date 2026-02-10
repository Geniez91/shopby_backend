package org.shopby_backend.article.persistence;

import org.shopby_backend.article.model.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    Boolean existsByName(String name);
}
