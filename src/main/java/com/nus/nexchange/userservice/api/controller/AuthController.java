package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.AuthenticationResponse;
import com.nus.nexchange.userservice.api.dto.UserDTO;
import com.nus.nexchange.userservice.application.security.RedisService;
import com.nus.nexchange.userservice.infrastructure.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-system/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisService redisService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUserEmail(), userDTO.getUserPassword())
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getUserEmail());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        long expiresIn = jwtUtil.getExpirationTime();
        return ResponseEntity.ok(new AuthenticationResponse(token, expiresIn));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // 从请求头中获取 JWT
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);

            // 从 Redis 中删除该 JWT
            redisService.deleteToken(jwt);
            return ResponseEntity.ok("Logged out successfully.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token or token missing.");
    }
}

