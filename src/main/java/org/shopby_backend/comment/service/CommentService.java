package org.shopby_backend.comment.service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.model.ArticleEntity;
import org.shopby_backend.article.persistence.ArticleRepository;
import org.shopby_backend.article.service.ArticleService;
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
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.service.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final CommentMapper commentMapper;
    private final CommentLikeMapper commentLikeMapper;
    private final ArticleService articleService;
    private final UsersService usersService;

    @Transactional
    public CommentOutputDto addComment(Long idArticle, CommentInputDto commentInputDto) {
        long start = System.nanoTime();

        if (commentRepository.existsByArticle_IdArticleAndUser_Id(idArticle, commentInputDto.idUser())) {
            CommentAlreadyExistsException exception = new CommentAlreadyExistsException(idArticle, commentInputDto.idUser());
            log.warn(LogMessages.COMMENT_ALREADY_EXISTS, idArticle, commentInputDto.idUser());
            throw exception;
        }

        ArticleEntity article = articleService.findArticleOrThrow(idArticle);
        UsersEntity user = usersService.findUsersOrThrow(commentInputDto.idUser());

        CommentEntity commentEntity = commentMapper.toEntity(commentInputDto, user, article);

        CommentEntity savedComment = commentRepository.save(commentEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le commentaire a bien été créer avec l'id {}, durationMs={}", savedComment.getIdComment(), durationMs);
        return commentMapper.toDto(savedComment);
    }

    @Transactional
    public CommentOutputDto updateComment(Long idComment, CommentUpdateDto commentUpdateDto) {
        long start = System.nanoTime();
        CommentEntity commentEntity = this.findCommentOrThrow(idComment);

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

    @Transactional
    public void deleteComment(Long idComment) {
        long start = System.nanoTime();
        CommentEntity commentEntity = this.findCommentOrThrow(idComment);

        commentRepository.delete(commentEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le commentaire a bien été supprimé avec l'id {}, durationMs={}", idComment, durationMs);
    }

    public Page<CommentOutputDto> getAllCommentsByArticleId(Long idArticle, Pageable pageable) {
        long start = System.nanoTime();
        Page<CommentEntity> page = commentRepository.findAllByArticle_IdArticle(idArticle,pageable);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le nombre de commentaire est de {}, page : {} pour l'article {},durationMs={}", page.getNumberOfElements(),page.getNumber(), idArticle, durationMs);
        return page.map(commentMapper::toDto);
    }

    public CommentOutputDto getCommentById(Long idComment) {
        long start = System.nanoTime();
        CommentEntity commentEntity= this.findCommentOrThrow(idComment);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le commentaire {} a bien été affiché, durationMs={}",idComment,durationMs);
        return commentMapper.toDto(commentEntity);
    }

    @Transactional
    public CommentLikeOutputDto addCommentLike(Long idComment, CommentLikeInputDto commentLikeInputDto) {
        long start = System.nanoTime();

        if(commentLikeRepository.existsByUser_IdAndComment_IdComment(commentLikeInputDto.idUser(), idComment)) {
            CommentLikeAlreadyExistsException exception = new CommentLikeAlreadyExistsException(commentLikeInputDto.idUser(), idComment);
            log.warn(LogMessages.COMMENT_LIKE_ALREADY_EXISTS, idComment,commentLikeInputDto.idUser());
            throw exception;
        }

        CommentEntity commentEntity = this.findCommentOrThrow(idComment);

        UsersEntity usersEntity = usersService.findUsersOrThrow(commentLikeInputDto.idUser());

        CommentLikeEntity commentLikeEntity = commentLikeMapper.toEntity(commentEntity, usersEntity);
        CommentLikeEntity savedCommentLike = commentLikeRepository.save(commentLikeEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le like sur le commentaire a bien été créer avec l'id {}, durationMs={}", savedCommentLike.getId(), durationMs);
        return commentLikeMapper.toDto(savedCommentLike);
    }

    @Transactional
    public void deleteCommentLike(Long idComment, CommentLikeInputDto commentLikeInputDto) {
        long start = System.nanoTime();

        CommentLikeEntity commentLikeEntity = findCommentLikeOrThrow(idComment,commentLikeInputDto);
        commentLikeRepository.delete(commentLikeEntity);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le like sur le commentaire a bien été supprimé avec l'id commentaire {}, idUser {}, durationMs={}", idComment,commentLikeInputDto.idUser(), durationMs);
    }

    public Long getLikeCount(Long idComment){
        long start = System.nanoTime();
        List<CommentLikeEntity> listCommentLikeEntity = commentLikeRepository.findByComment_IdComment(idComment);
        long likeCount=listCommentLikeEntity.size();
        long durationMs = Tools.getDurationMs(start);
        log.info("Le nombre de like commentaire est de {} pour le commentaire {},durationMs={}", likeCount, idComment, durationMs);
        return likeCount;
    }

    public CommentEntity findCommentOrThrow(Long idComment){
        return commentRepository.findById(idComment).orElseThrow(() -> {
            CommentNotFoundException exception = new CommentNotFoundException(idComment);
            log.warn(LogMessages.COMMENT_NOT_FOUND, idComment);
            return exception;
        });
    }

    public CommentLikeEntity findCommentLikeOrThrow(Long idComment, CommentLikeInputDto commentLikeInputDto){
        return commentLikeRepository.findByUser_IdAndComment_IdComment(commentLikeInputDto.idUser(),idComment).orElseThrow(()->{
            CommentLikeNotFoundException exception = new CommentLikeNotFoundException(idComment);
            log.warn(LogMessages.COMMENT_LIKE_NOT_FOUND, idComment);
            return exception;
        });
    }





}




