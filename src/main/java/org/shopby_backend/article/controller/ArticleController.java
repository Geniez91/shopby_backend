package org.shopby_backend.article.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.article.dto.*;
import org.shopby_backend.article.service.ArticleService;
import org.shopby_backend.tools.ErrorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/article")
@Tag(name = "Articles",description = "Gestion des articles")
public class ArticleController {
private final ArticleService articleService;

@Operation(summary = "Ajout d'un article")
@PreAuthorize("hasAnyAuthority('ARTICLE_CREATE')")
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
@ApiResponses({
        @ApiResponse(responseCode = "201", description = "Article créé avec succès"),
        @ApiResponse(
                responseCode = "400",
                description = "Données invalides",
                content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
        )),
        @ApiResponse(
                responseCode = "401",
                description = "Non authentifié",
                content = @Content(
                schema = @Schema(implementation = ErrorResponse.class)
        )),
        @ApiResponse(
                responseCode = "403",
                description = "Accès interdit",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class)
                )),
        @ApiResponse(
                responseCode = "409",
                description = "Conflit de données",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
public AddArticleOutputDto addArticle(@Valid @RequestBody AddArticleInputDto addArticleInputDto){
    return articleService.addNewArticle(addArticleInputDto);
}

@Operation(summary = "Mise à jour d'un article")
@PreAuthorize("hasAnyAuthority('ARTICLE_UPDATE')")
@PatchMapping("/{id}")
@ResponseStatus(HttpStatus.OK)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Article mis à jour"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Données invalides",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Article non trouvé",
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
        )
})
public AddArticleOutputDto updateArticle(@PathVariable Long id,@Valid @RequestBody AddArticleInputDto addArticleInputDto){
    return articleService.updateArticle(id, addArticleInputDto);
}

@Operation(summary = "Suppresion d'un article")
@PreAuthorize("hasAnyAuthority('ARTICLE_DELETE')")
@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
@ApiResponses({
        @ApiResponse(
                responseCode = "204",
                description = "Article supprimé"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Article non trouvé",
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
        )
})
public void deleteArticle(@PathVariable Long id){
     articleService.deleteArticle(id);
}

@DeleteMapping
@ResponseStatus(HttpStatus.OK)
@Operation(summary = "Récupérer tous les articles avec les filtres et pagination", description = "Permet de filtrer par prix, marque, type, nom")
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Articles récupérés avec succès"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Paramètres invalides",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
public Page<AddArticleOutputDto> getAllArticles(ArticleFilter filter, Pageable pageable){
    return articleService.getFilteredArticles(filter,pageable);
}

@Operation(summary = "Récupérer un article par id")
@GetMapping("/{id}")
@ResponseStatus(HttpStatus.OK)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Article trouvé"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Article non trouvé",
                content = @Content(
                        schema = @Schema(implementation = ErrorResponse.class)
                )
        )
})
public GetArticleOutputDto getArticleById(@PathVariable Long id){
    return articleService.getArticleById(id);
}

@GetMapping("/latest")
@ResponseStatus(HttpStatus.OK)
public List<GetArticlesOutputDto>getLastAddedArticles(){
return articleService.getLastFiveArticleAdded();
}
}
