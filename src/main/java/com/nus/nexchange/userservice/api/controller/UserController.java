package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.UserDTO;
import com.nus.nexchange.userservice.application.command.UserCommand;
import com.nus.nexchange.userservice.application.query.UserQuery;
import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-system/users")
public class UserController {

    @Autowired
    private UserQuery userQuery;

    @Autowired
    private UserCommand userCommand;

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
    public ResponseEntity<Void> resetUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}