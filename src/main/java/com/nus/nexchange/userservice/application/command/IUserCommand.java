package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.UserDTO;

public interface IUserCommand {
    void createUser(UserDTO userDTO);

    void resetPassword(UserDTO userDTO);
}
