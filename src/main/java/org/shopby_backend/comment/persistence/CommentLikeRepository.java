package org.shopby_backend.comment.persistence;

import org.shopby_backend.comment.model.CommentLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLikeEntity,Long> {
    boolean existsByUser_IdAndComment_IdComment(Long userId, Long commentId);
    Optional<CommentLikeEntity> findByUser_IdAndComment_IdComment(Long userId, Long commentId);
    List<CommentLikeEntity> findByComment_IdComment(Long commentId);
}
