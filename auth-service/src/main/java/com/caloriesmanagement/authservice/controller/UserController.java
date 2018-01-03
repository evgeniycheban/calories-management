package com.caloriesmanagement.authservice.controller;

import com.caloriesmanagement.authservice.model.User;
import com.caloriesmanagement.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    /**
     * @param service - user service.
     */
    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/current")
    public Principal current(Principal principal) {
        return principal;
    }

    @PreAuthorize("#oauth2.hasScope('server')")
    @PostMapping
    public void save(@Valid @RequestBody User user) {
        service.save(user);
    }
}
