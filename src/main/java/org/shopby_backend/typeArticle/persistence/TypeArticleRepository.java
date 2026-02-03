package org.shopby_backend.typeArticle.persistence;

import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeArticleRepository extends JpaRepository<TypeArticleEntity, Long> {
    Optional<TypeArticleEntity> findByLibelle(String libelle);
}
