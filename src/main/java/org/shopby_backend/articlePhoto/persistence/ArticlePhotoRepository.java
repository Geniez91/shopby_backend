package org.shopby_backend.articlePhoto.persistence;

import org.shopby_backend.articlePhoto.model.ArticlePhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticlePhotoRepository extends JpaRepository<ArticlePhotoEntity,Long> {
    @Query("SELECT COALESCE(MAX(p.position),0) from ArticlePhotoEntity p where p.article.idArticle=:articleId")
    Optional<Integer> findMaxPositionByArticleId(@Param("articleId") Long articleId);

    List<ArticlePhotoEntity> findByArticle_IdArticle(@Param("articleId") Long articleId);

    @Query("""
SELECT p FROM ArticlePhotoEntity p
WHERE p.article.idArticle IN :articleIds
AND p.position = 1
""")
    List<ArticlePhotoEntity> findCoversByArticleIds(List<Long> articleIds);
}
