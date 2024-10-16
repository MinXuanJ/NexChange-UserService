package com.nus.nexchange.userservice.api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AuthenticationResponse {
    private String token;
    private String tokenType = "Bearer ";
    private long expiresIn;
    private UUID userId;

    public AuthenticationResponse(String token, long expiresIn, UUID userId) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.userId = userId;
    }
}
