package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.AuthenticationResponse;
import com.nus.nexchange.userservice.api.dto.UserDTO;
import com.nus.nexchange.userservice.application.security.MyUserDetails;
import com.nus.nexchange.userservice.application.security.RedisService;
import com.nus.nexchange.userservice.infrastructure.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-system/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    private final RedisService redisService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService, RedisService redisService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.redisService = redisService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUserEmail(), userDTO.getUserPassword())
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or password");
        }

        final MyUserDetails userDetails = (MyUserDetails) userDetailsService.loadUserByUsername(userDTO.getUserEmail());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        long expiresIn = jwtUtil.getExpirationTime();
        return ResponseEntity.ok(new AuthenticationResponse(token, expiresIn, userDetails.getUserId()));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        // 从请求头中获取 JWT
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);

            // 从 Redis 中删除该 JWT
            redisService.deleteToken(jwt);
            return ResponseEntity.ok("Logged out successfully.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token or token missing.");
    }
}

