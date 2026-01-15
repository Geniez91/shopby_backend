package org.shopby_backend.article.persistence;

import org.shopby_backend.article.model.TypeArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeArticleRepository extends JpaRepository<TypeArticleEntity, Long> {
    TypeArticleEntity findById(long id);
}
