package org.shopby_backend.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represente les likes par commentaire d'un utilisateur affiché coté client")
public record CommentLikeOutputDto(Long idComment, Long idUser) {
}
