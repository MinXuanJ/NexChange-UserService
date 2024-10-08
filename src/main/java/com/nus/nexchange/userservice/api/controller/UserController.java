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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user-system/users")
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

    @PostMapping("/new-user")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
        UserIdentity user = modelMapper.map(userDTO, UserIdentity.class);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
        userCommand.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user")
    public ResponseEntity<UserDTO> getUser(@RequestBody UserDTO userDTO) {
        UserIdentity user = userQuery.getUserById(userDTO.getUserId());
        if (user != null) {
            UserDTO userDTOBack = modelMapper.map(user, UserDTO.class);
            return ResponseEntity.ok(userDTOBack);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}