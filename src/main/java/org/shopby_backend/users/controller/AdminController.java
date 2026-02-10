package org.shopby_backend.users.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.users.dto.UserUpdateRoleDto;
import org.shopby_backend.users.dto.UsersOutput;
import org.shopby_backend.users.service.UsersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@EnableMethodSecurity
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class AdminController {
    private final UsersService usersService;

    @PreAuthorize("hasAnyAuthority('USER_READ_ALL')")
    @GetMapping

    public List<UsersOutput> findAllUsers(){
        return this.usersService.findAllUsers();
    }


    @PreAuthorize("hasAnyAuthority('USER_UPDATE_ROLE')")
    @PutMapping
    public void updateUserRole(@RequestBody UserUpdateRoleDto userInputDto){
        this.usersService.updateUserRole(userInputDto);
    }
}
