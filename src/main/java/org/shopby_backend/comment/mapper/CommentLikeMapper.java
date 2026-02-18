package org.shopby_backend.comment.mapper;

import org.shopby_backend.comment.dto.CommentLikeOutputDto;
import org.shopby_backend.comment.model.CommentEntity;
import org.shopby_backend.comment.model.CommentLikeEntity;
import org.shopby_backend.users.model.UsersEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentLikeMapper {
    public CommentLikeEntity toEntity(CommentEntity commentEntity, UsersEntity usersEntity){
        return CommentLikeEntity.builder()
                .comment(commentEntity)
                .user(usersEntity)
                .build();
    }

    public CommentLikeOutputDto toDto(CommentLikeEntity commentLikeEntity){
        return new CommentLikeOutputDto(
                commentLikeEntity.getComment().getIdComment(),
                commentLikeEntity.getComment().getUser().getId()
        );
    }
}
