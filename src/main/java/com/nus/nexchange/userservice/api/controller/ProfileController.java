package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.ProfileDTO;
import com.nus.nexchange.userservice.application.command.ProfileCommand;
import com.nus.nexchange.userservice.application.query.ProfileQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user-system/profile")
public class ProfileController {
    private final ProfileQuery profileQuery;

    private final ProfileCommand profileCommand;

    @Autowired
    public ProfileController(ProfileQuery profileQuery, ProfileCommand profileCommand) {
        this.profileQuery = profileQuery;
        this.profileCommand = profileCommand;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileDTO> viewProfile(@PathVariable UUID userId) {
        try {
            ProfileDTO userProfile = profileQuery.getUserProfileByUserId(userId);
            return ResponseEntity.ok(userProfile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody ProfileDTO profileDTO) {
        try {
            profileCommand.updateProfile(profileDTO);
            return ResponseEntity.ok("Profile updated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
