package org.shopby_backend.users.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.jwt.service.JwtService;
import org.shopby_backend.users.dto.UserActivationDto;
import org.shopby_backend.users.dto.UserInputDto;
import org.shopby_backend.users.dto.UserLoginDto;
import org.shopby_backend.users.dto.UsersDto;
import org.shopby_backend.users.service.UsersService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/users")
    public String findAll(){
        return "test";
    }

    @PostMapping("/register")
    public UsersDto addUser(@RequestBody UserInputDto userInputDto){
        return usersService.addUser(userInputDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/activation")
    public String activateUser(@RequestBody UserActivationDto userActivationDto){
    return this.usersService.activationUser(userActivationDto.code());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody UserLoginDto userInputDto) {

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

}
