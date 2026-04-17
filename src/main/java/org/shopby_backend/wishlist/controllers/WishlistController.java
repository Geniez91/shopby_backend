package org.shopby_backend.wishlist.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.shopby_backend.article.dto.AddArticleOutputDto;
import org.shopby_backend.tools.ErrorResponse;
import org.shopby_backend.wishlist.dto.*;
import org.shopby_backend.wishlist.service.WishlistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/wishlist")
@Tag(name = "Wishlist",description = "Gestion des listes d'envies")
public class WishlistController {
    private WishlistService wishlistService;

    @Operation(summary = "Ajout d'une liste d'envie")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Création d'une liste d'envie effectué avec succès"),
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
                    description = "Utilisateur Conflict",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public WishlistOutputDto addNewWishlist(@Valid @RequestBody WishlistInputDto wishlistInputDto){
        return wishlistService.addWishList(wishlistInputDto);
    }

    @Operation(summary = "Mise à jour d'une liste d'envie")
    @PatchMapping("/{wishListId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mise à jour d'une liste d'envie effectué avec succès"
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
                    description = "Liste d'envie non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public WishlistOutputDto updateNewWishlist(@PathVariable Integer wishListId, @Valid @RequestBody WishlistUpdateDto wishlistUpdateDto){
        return wishlistService.updateWishlist(wishListId,wishlistUpdateDto);
    }

    @Operation(summary = "Suppresion d'une liste d'envie")
    @DeleteMapping("/{wishListId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Suppresion d'une liste d'envie effectué avec succès"),
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
                    description = "Liste d'envie non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public void deleteWishlist(@PathVariable Integer wishListId){
         wishlistService.deleteWishlist(wishListId);
    }

    @Operation(summary = "Récupérer une liste d'envie par id")
    @GetMapping("/{wishListId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Liste d'envie affiché avec succès"),
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
                    description = "Liste d'envie non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public WishlistOutputDto getWishlist(@PathVariable Integer wishListId){
        return wishlistService.getWishlist(wishListId);
    }

    @Operation(summary = "Récupérer les listes d'envie par filtre et pagination")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Listes d'envies affiché avec succès"),
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
                    description = "Liste d'envie par utilisateur non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public Page<WishlistOutputDto> getWishlists(WishlistFilter filter, WishListGetAllByIdDto wishListGetAllByIdDto,Pageable pageable){
        return wishlistService.getAllWishListByUserId(filter,wishListGetAllByIdDto,pageable);
    }

    @Operation(summary = "Ajout d'un article à une liste d'envie")
    @PostMapping("/{wishlistId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "L'article a bien été ajouté avec succès"),
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
                    description = "Liste d'envie non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "L'article existe deja dans la liste d'envie Conflict",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public WishlistAddItemOutputDto addNewListItem(@PathVariable Long wishlistId,@Valid @RequestBody WishlistAddItemInputDto wishlistAddItemInputDto){
        return wishlistService.addWishListItem(wishlistId,wishlistAddItemInputDto);
    }

    @Operation(summary = "Suppression d'un article à une liste d'envie")
    @DeleteMapping("/{wishlistId}/items")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Suppresion d'un article sur la liste d'envie a bien été ajouté avec succès"
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
                    description = "Liste d'envie non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public void deleteWishlistItem(@PathVariable Long wishlistId,@Valid @RequestBody WishlistAddItemInputDto wishlistAddItemInputDto){
         wishlistService.deleteWishlistItem(wishlistId,wishlistAddItemInputDto);
    }

    @Operation(summary = "Récupération de tous les articles par id liste d'envie")
    @GetMapping("{wishlistId}/items")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Affichage des articles de la liste d'envie avec succès"),
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
                    description = "Liste d'envie non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public Page<AddArticleOutputDto> getAllArticlesByWishlistId(@PathVariable Long wishlistId, Pageable pageable){
        return wishlistService.getAllArticleByWishlistId(wishlistId,pageable);
    }

    @PostMapping("/{wishlistId}/image")
    public WishlistImageUploadDto uploadWishlistImage(@PathVariable Integer wishlistId, @RequestPart("file") MultipartFile file){
        return wishlistService.uploadWishlistPhoto(wishlistId,file);
    }
}
