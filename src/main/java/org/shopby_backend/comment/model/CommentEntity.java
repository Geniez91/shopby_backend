package org.shopby_backend.comment.model;

import jakarta.persistence.*;
import lombok.*;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.users.model.UsersEntity;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comment")
    private Long idComment;

    @ManyToOne
    @JoinColumn(name = "id_article")
    private ArticleEntity article;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UsersEntity user;

    @Column(name = "date_comment")
    private Instant dateComment;

    private String description;

    private int note;

    @Version
    private Long version;
}
