package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.application.service.UserService;
import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserIdentity>> getAllUsers() {
        List<UserIdentity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserIdentity> createUser(@RequestBody UserIdentity user) {
        UserIdentity createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserIdentity> getUser(@PathVariable UUID id) {
        UserIdentity user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}