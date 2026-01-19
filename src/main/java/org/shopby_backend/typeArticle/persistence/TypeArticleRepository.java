package org.shopby_backend.typeArticle.persistence;

import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeArticleRepository extends JpaRepository<TypeArticleEntity, Long> {
    TypeArticleEntity findByLibelle(String libelle);
}
