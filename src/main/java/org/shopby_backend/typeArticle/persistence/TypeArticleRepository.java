package org.shopby_backend.typeArticle.persistence;

import org.shopby_backend.typeArticle.model.TypeArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TypeArticleRepository extends JpaRepository<TypeArticleEntity, Long>, JpaSpecificationExecutor<TypeArticleEntity> {
    boolean existsByLibelle(String libelle);
}
