package org.shopby_backend.comment.persistence;

import org.shopby_backend.comment.model.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    boolean existsByArticle_IdArticleAndUser_Id(Long idArticle, Long idUser);
   List<CommentEntity> findAllByArticle_IdArticle(Long idArticle);
}
