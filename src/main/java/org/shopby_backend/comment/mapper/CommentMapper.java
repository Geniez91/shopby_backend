package org.shopby_backend.comment.mapper;

import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.comment.dto.CommentInputDto;
import org.shopby_backend.comment.dto.CommentOutputDto;
import org.shopby_backend.comment.model.CommentEntity;
import org.shopby_backend.users.model.UsersEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public CommentEntity toEntity(CommentInputDto commentInputDto, UsersEntity usersEntity, ArticleEntity articleEntity){
        return CommentEntity.builder()
                .description(commentInputDto.description())
                .note(commentInputDto.note())
                .user(usersEntity)
                .article(articleEntity)
                .build();
    }

    public CommentOutputDto toDto(CommentEntity commentEntity){
        return new CommentOutputDto(
                commentEntity.getIdComment(),
                commentEntity.getArticle().getIdArticle(),
                commentEntity.getUser().getId(),
                commentEntity.getDateComment(),
                commentEntity.getDescription(),
                commentEntity.getNote()
        );
    }
}
