package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface IUserQuery {
    List<UserDTO> getAllUsers();

    UserDTO getUserById(UUID id);
}
