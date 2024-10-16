package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.UserDTO;

public interface IUserCommand {
    public void createUser(UserDTO userDTO);
    public void resetPassword(UserDTO userDTO);
}
