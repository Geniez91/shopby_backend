package org.shopby_backend.article.persistence;

import org.shopby_backend.article.model.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    Boolean existsByName(String name);
    Page<ArticleEntity> findAll(Pageable pageable);
}
