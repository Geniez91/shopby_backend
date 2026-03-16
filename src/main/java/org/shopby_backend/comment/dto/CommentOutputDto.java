package org.shopby_backend.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Représente le commentaire affiché coté client")
public record CommentOutputDto(Long idComment, Long idArticle, Long idUser, Instant dateComment,String description,Integer note,Long version) {
}
