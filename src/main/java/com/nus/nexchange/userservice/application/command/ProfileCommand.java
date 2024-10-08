package com.nus.nexchange.userservice.application.command;


import com.nus.nexchange.userservice.domain.aggregate.UserProfile;
import com.nus.nexchange.userservice.infrastructure.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileCommand implements IProfileCommand {

    @Autowired
    private UserProfileRepository profileRepository;

    @Override
    public void createProfile(UserProfile profile) {
        profileRepository.save(profile);
    }

    @Override
    public void updateProfile(UserProfile profile) {
        profileRepository.save(profile);
    }

    @Override
    public void deleteProfile(UUID id) {
        profileRepository.deleteById(id);
    }
}
