package org.shopby_backend.comment.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.comment.dto.*;
import org.shopby_backend.comment.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class CommentController {
    private CommentService commentService;


    @PostMapping("/article/{idArticle}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentOutputDto addComment(@PathVariable Long idArticle, CommentInputDto commentInputDto){
        return commentService.addComment(idArticle,commentInputDto);
    }

    @PatchMapping("/comments/{idComment}")
    @ResponseStatus(HttpStatus.OK)
    public CommentOutputDto updateComment(@PathVariable Long idComment, CommentUpdateDto commentUpdateDto){
        return commentService.updateComment(idComment,commentUpdateDto);
    }

    @DeleteMapping("/comments/{idComment}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long idComment){
        commentService.deleteComment(idComment);
    }

    @GetMapping("/article/{idArticle}/comments")
    @ResponseStatus(HttpStatus.OK)
    public Page<CommentOutputDto> getAllCommentsByArticleId(@PathVariable Long idArticle, Pageable pageable){
        return commentService.getAllCommentsByArticleId(idArticle,pageable);
    }

    @GetMapping("/comments/{idComment}")
    @ResponseStatus(HttpStatus.OK)
    public CommentOutputDto getCommentById(@PathVariable Long idComment){
        return commentService.getCommentById(idComment);
    }

    @PostMapping("/comments/{idComment}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentLikeOutputDto addCommentLike(@PathVariable Long idComment, CommentLikeInputDto commentLikeInputDto){
        return commentService.addCommentLike(idComment,commentLikeInputDto);
    }

   @DeleteMapping("/comments/{idComment}/like")
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void deleteCommentLike(@PathVariable Long idComment, CommentLikeInputDto commentLikeInputDto){
         commentService.deleteCommentLike(idComment,commentLikeInputDto);
   }

   @GetMapping("/comments/{idComment}/like")
   @ResponseStatus(HttpStatus.OK)
   public Long getLikeCount(@PathVariable Long idComment){
        return commentService.getLikeCount(idComment);
   }




}
