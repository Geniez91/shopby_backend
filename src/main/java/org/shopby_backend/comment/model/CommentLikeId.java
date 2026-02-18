package org.shopby_backend.comment.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class CommentLikeId {
    private Long idComment;
    private Long idUser;
}
