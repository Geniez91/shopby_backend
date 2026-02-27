package org.shopby_backend.comment.persistence;

import org.shopby_backend.comment.model.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    boolean existsByArticle_IdArticleAndUser_Id(Long idArticle, Long idUser);
   Page<CommentEntity> findAllByArticle_IdArticle(Long idArticle,Pageable pageable);
}
