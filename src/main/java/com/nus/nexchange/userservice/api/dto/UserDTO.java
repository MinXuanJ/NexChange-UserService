package com.nus.nexchange.userservice.api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {
    private UUID userId;
    private String userName;
    private String userEmail;
    private String userPassword;
}
