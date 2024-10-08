package com.nus.nexchange.userservice.api.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String token;
    private String tokenType = "Bearer ";
    private long expiresIn;

    public AuthenticationResponse(String token, long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
