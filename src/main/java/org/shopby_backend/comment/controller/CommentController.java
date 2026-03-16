package org.shopby_backend.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.shopby_backend.comment.dto.*;
import org.shopby_backend.comment.service.CommentService;
import org.shopby_backend.tools.ErrorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "Comment", description = "Gestion des commentaires par article")
public class CommentController {
    private CommentService commentService;

    @Operation(summary = "Ajout d'un commentaire")
    @PostMapping("/article/{idArticle}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Commentaire créé avec succès"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non authentifié",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès interdit",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflit de données",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public CommentOutputDto addComment(@PathVariable Long idArticle, CommentInputDto commentInputDto){
        return commentService.addComment(idArticle,commentInputDto);
    }

    @PatchMapping("/comments/{idComment}")
    @Operation(summary = "Mise à jour d'un commentaire")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mise à jour d'un commentaire effectués avec succès"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non authentifié",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès interdit",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Commentaire non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @ResponseStatus(HttpStatus.OK)
    public CommentOutputDto updateComment(@PathVariable Long idComment, CommentUpdateDto commentUpdateDto){
        return commentService.updateComment(idComment,commentUpdateDto);
    }

    @Operation(summary = "Suppresion d'un commentaire")
    @DeleteMapping("/comments/{idComment}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Suppresion effectués avec succès"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non authentifié",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès interdit",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Commentaire non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public void deleteComment(@PathVariable Long idComment){
        commentService.deleteComment(idComment);
    }

    @Operation(summary = "Récupérer tous les commentaires par l'id d'un article et pagination")
    @GetMapping("/article/{idArticle}/comments")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Affichage avec succès de tous les commentaires"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Article non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public Page<CommentOutputDto> getAllCommentsByArticleId(@PathVariable Long idArticle, Pageable pageable){
        return commentService.getAllCommentsByArticleId(idArticle,pageable);
    }

    @Operation(summary = "Recuperer un commentaire par son id")
    @GetMapping("/comments/{idComment}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Affichage avec succès de tous les commentaires"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Commentaire non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public CommentOutputDto getCommentById(@PathVariable Long idComment){
        return commentService.getCommentById(idComment);
    }

    @Operation(summary = "Ajout d'un like pour un commentaire")
    @PostMapping("/comments/{idComment}/like")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Un like a bien été ajouté au commentaire"),
            @ApiResponse(
                    responseCode = "401",
                    description = "Non authentifié",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Accès interdit",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Commentaire non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflit de données",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public CommentLikeOutputDto addCommentLike(@PathVariable Long idComment, CommentLikeInputDto commentLikeInputDto){
        return commentService.addCommentLike(idComment,commentLikeInputDto);
    }

   @Operation(summary = "Suppresion d'un like pour un commentaire")
   @DeleteMapping("/comments/{idComment}/like")
   @ApiResponses({
           @ApiResponse(
                   responseCode = "204",
                   description = "Suppresion d'un like sur un commentaire avec succès"),
           @ApiResponse(
                   responseCode = "404",
                   description = "Like sur un commentaire non trouvé",
                   content = @Content(
                           schema = @Schema(implementation = ErrorResponse.class)
                   )
           )
   })
   @ResponseStatus(HttpStatus.NO_CONTENT)
   public void deleteCommentLike(@PathVariable Long idComment, CommentLikeInputDto commentLikeInputDto){
         commentService.deleteCommentLike(idComment,commentLikeInputDto);
   }

   @Operation(summary = "Récupérer tous les like d'un commentaire")
   @GetMapping("/comments/{idComment}/like")
   @ResponseStatus(HttpStatus.OK)
   @ApiResponses({
           @ApiResponse(
                   responseCode = "204",
                   description = "Suppresion d'un like sur un commentaire avec succès"),
           @ApiResponse(
                   responseCode = "404",
                   description = "Like sur un commentaire non trouvé",
                   content = @Content(
                           schema = @Schema(implementation = ErrorResponse.class)
                   )
           )
   })
   public Long getLikeCount(@PathVariable Long idComment){
        return commentService.getLikeCount(idComment);
   }




}
