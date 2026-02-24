package org.shopby_backend.comment.dto;

import java.time.Instant;

public record CommentOutputDto(Long idComment, Long idArticle, Long idUser, Instant dateComment,String description,Integer note,Long version) {
}
