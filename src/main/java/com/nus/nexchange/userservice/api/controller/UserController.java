package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.UserDTO;
import com.nus.nexchange.userservice.application.command.UserCommand;
import com.nus.nexchange.userservice.application.query.UserQuery;
import com.nus.nexchange.userservice.application.security.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-system/users")
public class UserController {

    private final UserQuery userQuery;

    private final UserCommand userCommand;

    private final RedisService redisService;

    @Autowired
    public UserController(UserQuery userQuery, UserCommand userCommand, RedisService redisService) {
        this.userQuery = userQuery;
        this.userCommand = userCommand;
        this.redisService = redisService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userQuery.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") UUID userId) {
        try {
            UserDTO user = userQuery.getUserById(userId);
            return ResponseEntity.ok(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/new-user")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        userCommand.createUser(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/reset")
    public ResponseEntity<String> resetUser(@RequestHeader("Authorization") String token, @RequestBody UserDTO userDTO) {
        try {
            userCommand.resetPassword(userDTO);
            redisService.deleteToken(token.substring(7));
            return ResponseEntity.ok("Password reset successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}