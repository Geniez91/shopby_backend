package org.shopby_backend.comment.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.comment.dto.*;
import org.shopby_backend.comment.mapper.CommentLikeMapper;
import org.shopby_backend.comment.mapper.CommentMapper;
import org.shopby_backend.comment.model.CommentEntity;
import org.shopby_backend.comment.model.CommentLikeEntity;
import org.shopby_backend.comment.persistence.CommentLikeRepository;
import org.shopby_backend.comment.persistence.CommentRepository;
import org.shopby_backend.exception.article.ArticleNotFoundException;
import org.shopby_backend.exception.comment.CommentAlreadyExistsException;
import org.shopby_backend.exception.comment.CommentLikeAlreadyExistsException;
import org.shopby_backend.exception.comment.CommentLikeNotFoundException;
import org.shopby_backend.exception.comment.CommentNotFoundException;
import org.shopby_backend.exception.users.UsersNotFoundException;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.persistence.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class CommentService {
    private CommentRepository commentRepository;
    private ArticleRepository articleRepository;
    private UsersRepository usersRepository;
    private CommentLikeRepository commentLikeRepository;
    private CommentMapper commentMapper;
    private CommentLikeMapper commentLikeMapper;

    public CommentOutputDto addComment(Long idArticle, CommentInputDto commentInputDto) {
        long start = System.nanoTime();

        if (commentRepository.existsByIdArticleAndIdUser(idArticle, commentInputDto.idUser())) {
            CommentAlreadyExistsException exception = new CommentAlreadyExistsException(idArticle, commentInputDto.idUser());
            log.warn(LogMessages.COMMENT_ALREADY_EXISTS, idArticle, commentInputDto.idUser());
            throw exception;
        }

        ArticleEntity article = articleRepository.findById(idArticle).orElseThrow(() -> {
            ArticleNotFoundException exception = new ArticleNotFoundException(idArticle);
            log.warn(LogMessages.ARTICLE_NOT_FOUND, idArticle);
            return exception;
        });

        UsersEntity user = usersRepository.findById(commentInputDto.idUser()).orElseThrow(() -> {
            UsersNotFoundException exception = UsersNotFoundException.byUserId(commentInputDto.idUser());
            log.warn(LogMessages.USERS_NOT_FOUND_BY_USER_ID, commentInputDto.idUser(), exception);
            return exception;
        });

        CommentEntity commentEntity = commentMapper.toEntity(commentInputDto, user, article);

        CommentEntity savedComment = commentRepository.save(commentEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le commentaire a bien été créer avec l'id {}, durationMs={}", savedComment.getIdComment(), durationMs);
        return commentMapper.toDto(savedComment);
    }

    public CommentOutputDto updateComment(Long idComment, CommentUpdateDto commentUpdateDto) {
        long start = System.nanoTime();
        CommentEntity commentEntity = commentRepository.findById(idComment).orElseThrow(() -> {
            CommentNotFoundException exception = new CommentNotFoundException(idComment);
            log.warn(LogMessages.COMMENT_NOT_FOUND, idComment);
            return exception;
        });

        if (commentUpdateDto.description() != null) {
            commentEntity.setDescription(commentUpdateDto.description());
        }

        if (commentUpdateDto.note() != null) {
            commentEntity.setNote(commentUpdateDto.note());
        }

        CommentEntity savedComment = commentRepository.save(commentEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le commentaire a bien été mise à jour avec l'id {}, durationMs={}", savedComment.getIdComment(), durationMs);
        return commentMapper.toDto(savedComment);
    }

    public void deleteComment(Long idComment) {
        long start = System.nanoTime();
        CommentEntity commentEntity = commentRepository.findById(idComment).orElseThrow(() -> {
            CommentNotFoundException exception = new CommentNotFoundException(idComment);
            log.warn(LogMessages.COMMENT_NOT_FOUND, idComment);
            return exception;
        });

        commentRepository.delete(commentEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le commentaire a bien été supprimé avec l'id {}, durationMs={}", idComment, durationMs);
    }

    public List<CommentOutputDto> getAllCommentsByArticleId(Long idArticle) {
        long start = System.nanoTime();
        List<CommentOutputDto> commentOutputDto = commentRepository.findAllByIdArticle(idArticle).stream().map((commentEntity) ->commentMapper.toDto(commentEntity)).toList();
        long durationMs = Tools.getDurationMs(start);
        log.info("Le nombre de commentaire est de {} pour l'article {},durationMs={}", commentOutputDto.size(), idArticle, durationMs);
        return commentOutputDto;
    }

    public CommentOutputDto getCommentById(Long idComment) {
        long start = System.nanoTime();
        CommentEntity commentEntity= commentRepository.findById(idComment).orElseThrow(()->{
            CommentNotFoundException exception = new CommentNotFoundException(idComment);
            log.warn(LogMessages.COMMENT_NOT_FOUND, idComment);
            return exception;
        });
        long durationMs = Tools.getDurationMs(start);
        log.info("Le commentaire {} a bien été affiché, durationMs={}",idComment,durationMs);
        return commentMapper.toDto(commentEntity);
    }

    public CommentLikeOutputDto addCommentLike(Long idComment, CommentLikeInputDto commentLikeInputDto) {
        long start = System.nanoTime();

        if(commentLikeRepository.existsByUserIdAndCommentId(commentLikeInputDto.idUser(), idComment)) {
            CommentLikeAlreadyExistsException exception = new CommentLikeAlreadyExistsException(commentLikeInputDto.idUser(), idComment);
            log.warn(LogMessages.COMMENT_LIKE_ALREADY_EXISTS, idComment,commentLikeInputDto.idUser());
        }

        CommentEntity commentEntity = commentRepository.findById(idComment).orElseThrow(()->{
            CommentNotFoundException exception = new CommentNotFoundException(idComment);
            log.warn(LogMessages.COMMENT_NOT_FOUND, idComment);
            return exception;
        });

        UsersEntity usersEntity = usersRepository.findById(commentLikeInputDto.idUser()).orElseThrow(()->{
            UsersNotFoundException exception = UsersNotFoundException.byUserId(commentLikeInputDto.idUser());
            log.warn(LogMessages.USERS_NOT_FOUND_BY_USER_ID, commentLikeInputDto.idUser());
            return exception;
        });

        CommentLikeEntity commentLikeEntity = commentLikeMapper.toEntity(commentEntity, usersEntity);
        CommentLikeEntity savedCommentLike = commentLikeRepository.save(commentLikeEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le like sur le commentaire a bien été créer avec l'id {}, durationMs={}", savedCommentLike.getId(), durationMs);
        return commentLikeMapper.toDto(savedCommentLike);
    }

    public void deleteCommentLike(Long idComment, CommentLikeInputDto commentLikeInputDto) {
        long start = System.nanoTime();

        CommentLikeEntity commentLikeEntity = commentLikeRepository.findByUserIdAndCommentId(commentLikeInputDto.idUser(),idComment).orElseThrow(()->{
            CommentLikeNotFoundException exception = new CommentLikeNotFoundException(idComment);
            log.warn(LogMessages.COMMENT_LIKE_NOT_FOUND, idComment);
            return exception;
        });
        commentLikeRepository.delete(commentLikeEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le like sur le commentaire a bien été supprimé avec l'id commentaire {}, idUser {}, durationMs={}", idComment,commentLikeInputDto.idUser(), durationMs);
    }

    public Long getLikeCount(Long idComment){
        long start = System.nanoTime();
        List<CommentLikeEntity> listCommentLikeEntity = commentLikeRepository.findByCommentId(idComment);
        long likeCount=listCommentLikeEntity.size();
        long durationMs = Tools.getDurationMs(start);
        log.info("Le nombre de like commentaire est de {} pour le commentaire {},durationMs={}", likeCount, idComment, durationMs);
        return likeCount;
    }





}




