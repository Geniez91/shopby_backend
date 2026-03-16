package org.shopby_backend.typeArticle.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.shopby_backend.tools.ErrorResponse;
import org.shopby_backend.typeArticle.dto.TypeArticleDto;
import org.shopby_backend.typeArticle.dto.TypeArticleFilter;
import org.shopby_backend.typeArticle.dto.TypeArticleOutputDto;
import org.shopby_backend.typeArticle.service.TypeArticleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/type_article")
@Tag(name = "Type Article",description = "Gestion des types d'articles")
public class TypeArticleController {
    private TypeArticleService typeArticleService;

    @Operation(summary = "Ajout d'un type d'article")
    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_CREATE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Type Article créé avec succès"),
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
                    responseCode = "409",
                    description = "Conflit de données",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )

            )
    })
    public TypeArticleOutputDto addTypeArticle(@Valid @RequestBody TypeArticleDto typeArticleDto) {
        return typeArticleService.addTypeArticle(typeArticleDto);
    }

    @Operation(summary = "Mise à jour d'un type article")
    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_UPDATE')")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Type Article mise à jour avec succès"),
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
                    description = "Type article non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public TypeArticleOutputDto updateTypeArticle(@PathVariable Long id, @Valid @RequestBody TypeArticleDto typeArticleDto) {
        return typeArticleService.updateTypeArticle(id, typeArticleDto);
    }

    @Operation(summary = "Suppresion d'un type article")
    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_DELETE')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Type article supprimé avec succès"
            ),
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
                    description = "Type article non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public void deleteTypeArticle(@PathVariable Long id) {
         typeArticleService.deleteTypeArticle(id);
    }

    @Operation(summary = "Récupérer tous les types article avec filtre et pagination", description = "Permet d'etre filtrer par libelle")
    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_READ_ALL')")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Type article affiché avec succès"),
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
    })
    @GetMapping
    public Page<TypeArticleOutputDto> getAllTypeArticle(TypeArticleFilter filter, Pageable pageable) {
        return typeArticleService.getAllTypeArticle(filter, pageable);
    }

    @Operation(summary = "Récupérer le type article par id")
    @PreAuthorize("hasAnyAuthority('TYPE_ARTICLE_READ')")
    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Type article affiché avec succès"),
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
                    description = "Type article non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public TypeArticleOutputDto getTypeArticleById(@PathVariable Long id) {
        return typeArticleService.getTypeArticleById(id);
    }


}
