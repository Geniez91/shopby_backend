package org.shopby_backend.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.jwt.service.JwtService;
import org.shopby_backend.tools.ErrorResponse;
import org.shopby_backend.users.dto.*;
import org.shopby_backend.users.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.Map;

@Slf4j
@EnableMethodSecurity
@AllArgsConstructor
@RestController
@Tag(name = "User",description = "Gestion des utilisateurs")
public class UsersController {

    private AuthenticationManager authenticationManager;
    private final UsersService usersService;
    private final JwtService jwtService;

    @Operation(summary = "Inscription d'un utilisateur")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Utilisateurs affiché avec succès"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides",
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
    public UsersDto addUser(@Valid @RequestBody UserInputDto userInputDto){
        return usersService.addUser(userInputDto);
    }

    @Operation(summary = "Activation du compte")
    @PostMapping("/activation")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Activation du compte effectué avec succès"),
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
    public String activateUser(@Valid @RequestBody UserActivationDto userActivationDto){
    return this.usersService.activationUser(userActivationDto.code());
    }

    @Operation(summary = "Connexion utilisateur")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Connexion effectué avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Accès interdit"),
    })
    public Map<String, String> loginUser(@Valid @RequestBody UserLoginDto userInputDto) {
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    userInputDto.email(),
                                    userInputDto.password()
                            )
                    );
            return jwtService.generateToken(userInputDto.email());
        } catch (Exception e) {
            log.error("Erreur d'authentification", e);
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
    }

    @Operation(summary = "Déconnexion d'un utilisateur")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/logOut")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Déconnexion effectué avec succès"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides",
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
    public void logOut(){
        this.jwtService.logOut();
    }

    @Operation(summary = "Réinitialisation de mot de passe")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reset-password")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Réinitialisation de mot de passe effectué avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides",
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
    public void resetPassword(@Valid @RequestBody UserResetDto userResetDto){
        usersService.resetPassword(userResetDto);
    }

    @Operation(summary = "Mise en place d'un nouveau mot de passe")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/new-password")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Mise en place d'un nouveau mot de passe effectué avec succès"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides",
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
    })
    public void newPassword(@Valid @RequestBody UserNewPasswordDto userNewPasswordDto){
        usersService.newPassword(userNewPasswordDto);
    }

    @Operation(summary = "Mise à jour des informations de l'utilisateur")
    @PreAuthorize("hasAnyAuthority('USER_UPDATE')")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mise à jour des infos de l'utilisateur effectué avec succès"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Données invalides",
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
                    description = "Utilisateur non trouvé",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
    })
    @PatchMapping("/user/{userId}")
    public UsersDto updateUserInfo(@PathVariable Long userId,@RequestBody UserInfoUpdateDto userInfoUpdate){
        return usersService.updateUserInfo(userId,userInfoUpdate);
    }


}
