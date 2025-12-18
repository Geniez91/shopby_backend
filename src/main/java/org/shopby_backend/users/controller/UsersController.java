package org.shopby_backend.users.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.users.dto.UserActivationDto;
import org.shopby_backend.users.dto.UserInputDto;
import org.shopby_backend.users.dto.UsersDto;
import org.shopby_backend.users.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@EnableMethodSecurity
@AllArgsConstructor
@RestController
public class UsersController {
    private final UsersService usersService;

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
}
