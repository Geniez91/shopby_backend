package org.shopby_backend.users.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class usersController {
    @GetMapping()
    public String findAll(){
        return "test";
    }
}
