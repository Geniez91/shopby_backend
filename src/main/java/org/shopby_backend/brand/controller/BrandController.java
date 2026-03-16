package org.shopby_backend.brand.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.shopby_backend.brand.dto.BrandFilter;
import org.shopby_backend.brand.dto.BrandInputDto;
import org.shopby_backend.brand.dto.BrandOutputDto;
import org.shopby_backend.brand.service.BrandService;
import org.shopby_backend.tools.ErrorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/brand")
@Tag(name = "Brand", description = "Gestion des marques")
public class BrandController {
    private BrandService brandService;

    @Operation(summary = "Ajout d'une marque")
    @PreAuthorize("hasAnyAuthority('BRAND_CREATE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Marque créé avec succès"),
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
                    )
            ),
            @ApiResponse(responseCode = "409",
                    description = "Conflit de données",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public BrandOutputDto addBrand(@Valid @RequestBody BrandInputDto brandInputDto){
        return brandService.addBrand(brandInputDto);
    }

    @Operation(summary = "Mise à jour d'une marque")
    @PreAuthorize("hasAnyAuthority('BRAND_UPDATE')")
    @PatchMapping("/{brandId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Marque mis à jour"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Marque non trouvé",
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
    public BrandOutputDto updateBrand(@PathVariable Long brandId,@Valid @RequestBody BrandInputDto brandInputDto){
        return brandService.updateBrand(brandId,brandInputDto);
    }

    @Operation(summary = "Récupérer tous les marques avec filtres et pagination", description = "Permet d'etre filtrer par libelle")
    @PreAuthorize("hasAnyAuthority('BRAND_READ_ALL')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Marques récupérés avec succès"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Paramètres invalides",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    public Page<BrandOutputDto> getAllBrands(BrandFilter brandFilter, Pageable pageable){
        return brandService.findAllBrands(brandFilter, pageable);
    }

    @Operation(summary = "Recupérer une marque par id")
    @PreAuthorize("hasAnyAuthority('BRAND_READ')")
    @GetMapping("/{brandId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Marque récupérés avec succès"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Marque non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public BrandOutputDto getBrand(@PathVariable Long brandId){
        return brandService.findBrandById(brandId);
    }

    @Operation(summary = "Suppresion d'une marque")
    @PreAuthorize("hasAnyAuthority('BRAND_DELETE')")
    @DeleteMapping("/{brandId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Marque supprimé avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Paramètres invalides",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Marque non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    public void deleteBrand(@PathVariable Long brandId){
         brandService.deleteBrand(brandId);
    }


}
