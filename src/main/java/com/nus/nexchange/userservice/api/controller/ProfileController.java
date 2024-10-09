package com.nus.nexchange.userservice.api.controller;

import com.nus.nexchange.userservice.api.dto.ProfileDTO;
import com.nus.nexchange.userservice.api.dto.UserDTO;
import com.nus.nexchange.userservice.application.command.ProfileCommand;
import com.nus.nexchange.userservice.application.query.ProfileQuery;
import com.nus.nexchange.userservice.domain.aggregate.UserProfile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user-system/profile")
public class ProfileController {
    @Autowired
    private ProfileQuery profileQuery;

    @Autowired
    private ProfileCommand profileCommand;

    @PostMapping
    public ResponseEntity<ProfileDTO> viewProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO userProfile = profileQuery.getUserProfileByUserId(profileDTO.getUserId());

        return ResponseEntity.ok(userProfile);
    }

//    @PostMapping("/newProfile")
//    public ResponseEntity<String> createProfile(@RequestBody ProfileDTO profileDTO) {
//        UserProfile userProfile = modelMapper.map(profileDTO, UserProfile.class);
//        profileCommand.createProfile(userProfile);
//        return ResponseEntity.ok("Profile created");
//    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody ProfileDTO profileDTO) {
        try {
            profileCommand.updateProfile(profileDTO);
            return ResponseEntity.ok("Profile updated");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteProfile(@RequestParam UUID userId) {
//        profileCommand.deleteProfile(userId);
//        return ResponseEntity.ok("Profile deleted");
//    }
}
