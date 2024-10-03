package com.nus.nexchange.userservice.application.controllers;

import com.nus.nexchange.userservice.domain.domainmodels.aggregates.UserIdentity;
import com.nus.nexchange.userservice.domain.services.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserDomainService userDomainService;

    @Autowired
    public UserController(UserDomainService userDomainService) {
        this.userDomainService = userDomainService;
    }

    @GetMapping
    public ResponseEntity<List<UserIdentity>> getAllUsers() {
        List<UserIdentity> users = userDomainService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @PostMapping
    public ResponseEntity<UserIdentity> createUser(@RequestBody UserIdentity user) {
        UserIdentity createdUser = userDomainService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserIdentity> getUser(@PathVariable UUID id) {
        UserIdentity user = userDomainService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}