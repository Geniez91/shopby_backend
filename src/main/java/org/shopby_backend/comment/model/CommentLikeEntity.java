package org.shopby_backend.comment.model;

import jakarta.persistence.*;
import lombok.*;
import org.shopby_backend.users.model.UsersEntity;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "comment_like")
public class CommentLikeEntity {
    @EmbeddedId
    private CommentLikeId id;

    @ManyToOne
    @MapsId("idComment")
    @JoinColumn(name = "id_comment")
    private CommentEntity comment;

    @ManyToOne
    @MapsId("idUser")
    @JoinColumn(name = "id_user")
    private UsersEntity user;
}
