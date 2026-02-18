package org.shopby_backend.comment.dto;

import jakarta.validation.constraints.NotNull;

public record CommentInputDto(@NotNull Long idUser, String description, @NotNull Integer note) {
}
