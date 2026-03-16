package org.shopby_backend.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.tools.ErrorResponse;
import org.shopby_backend.users.dto.UserFilter;
import org.shopby_backend.users.dto.UserUpdateRoleDto;
import org.shopby_backend.users.dto.UsersDto;
import org.shopby_backend.users.dto.UsersOutput;
import org.shopby_backend.users.service.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@EnableMethodSecurity
@AllArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "Admin",description = "Gestion administrateur des utilisateurs")
public class AdminController {
    private final UsersService usersService;

    @Operation(summary = "Récupération des utilisateurs avec filtre et pagination")
    @PreAuthorize("hasAnyAuthority('USER_READ_ALL')")
    @GetMapping
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateurs affiché avec succès"),
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
            )
    })
    public Page<UsersDto> findAllUsers(UserFilter filter, Pageable pageable){
        return this.usersService.findAllUsers(filter, pageable);
    }

    @Operation(summary = "Mise à jour du role des utilisateurs")
    @PreAuthorize("hasAnyAuthority('USER_UPDATE')")
    @PutMapping
    public void updateUserRole(@RequestBody UserUpdateRoleDto userInputDto){
        this.usersService.updateUserRole(userInputDto);
    }
}
