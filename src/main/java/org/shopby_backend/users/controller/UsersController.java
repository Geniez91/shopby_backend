package org.shopby_backend.users.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.jwt.service.JwtService;
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
public class UsersController {

    private AuthenticationManager authenticationManager;
    private final UsersService usersService;
    private final JwtService jwtService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UsersDto addUser(@Valid @RequestBody UserInputDto userInputDto){
        return usersService.addUser(userInputDto);
    }


    @PostMapping("/activation")
    @ResponseStatus(HttpStatus.CREATED)
    public String activateUser(@Valid @RequestBody UserActivationDto userActivationDto){
    return this.usersService.activationUser(userActivationDto.code());
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/logOut")
    public void logOut(){
        this.jwtService.logOut();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody UserResetDto userResetDto){
        usersService.resetPassword(userResetDto);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/new-password")
    public void newPassword(@Valid @RequestBody UserNewPasswordDto userNewPasswordDto){
        usersService.newPassword(userNewPasswordDto);
    }
    @PreAuthorize("hasAnyAuthority('USER_UPDATE')")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/user/{userId}")
    public UserOutputInfoUpdateDto updateUserInfo(@PathVariable Long userId,@RequestBody UserInfoUpdateDto userInfoUpdate){
        return usersService.updateUserInfo(userId,userInfoUpdate);
    }

}
