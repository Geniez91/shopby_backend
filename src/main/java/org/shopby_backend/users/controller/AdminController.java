package org.shopby_backend.users.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.users.dto.UserUpdateRoleDto;
import org.shopby_backend.users.dto.UsersOutput;
import org.shopby_backend.users.service.UsersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@EnableMethodSecurity
@AllArgsConstructor
@RestController
public class AdminController {
    private final UsersService usersService;

    @PreAuthorize("hasAnyAuthority('ADMIN_READ')")
    @GetMapping("/users")
    public List<UsersOutput> findAllUsers(){
        return this.usersService.findAllUsers();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_READ')")
    @PutMapping("/users")
    public void updateUserRole(@RequestBody UserUpdateRoleDto userInputDto){
        this.usersService.updateUserRole(userInputDto);
    }
}
