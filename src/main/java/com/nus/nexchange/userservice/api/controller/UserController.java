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
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserQuery userQuery;

    @Autowired
    private UserCommand userCommand;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserIdentity> users = userQuery.getAllUsers();

        List<UserDTO> userDTOs = users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDTOs);
    }

    @PostMapping("/userCreate")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        UserIdentity user = modelMapper.map(userDTO, UserIdentity.class);
//        user.setUserId(UUID.randomUUID());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
        userCommand.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
        UserIdentity user = userQuery.getUserById(id);
        if (user != null) {
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}