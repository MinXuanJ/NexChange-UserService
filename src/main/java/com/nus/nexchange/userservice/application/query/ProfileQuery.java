package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;
import com.nus.nexchange.userservice.domain.aggregate.UserProfile;
import com.nus.nexchange.userservice.infrastructure.repository.UserProfileRepository;
import com.nus.nexchange.userservice.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProfileQuery implements IProfileQuery {

    @Autowired
    private UserProfileRepository profileRepository;

    @Override
    public UserProfile getUserProfileByUserId(UUID userId) {
        return profileRepository.findByUserId(userId);
    }
}
