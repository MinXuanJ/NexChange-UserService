package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.ProfileDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserProfile;
import com.nus.nexchange.userservice.infrastructure.repository.UserProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileQuery implements IProfileQuery {

    private final UserProfileRepository profileRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ProfileQuery(UserProfileRepository profileRepository, ModelMapper modelMapper) {
        this.profileRepository = profileRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProfileDTO getUserProfileByUserId(UUID userId) {
        UserProfile profile = profileRepository.findByUserId(userId);
        if (profile == null) {
            throw new IllegalArgumentException("Profile not found");
        }
        return modelMapper.map(profile, ProfileDTO.class);
    }

//    @Override
//    public ProfileDTO getProfile(UUID profileId){
//        UserProfile profile = profileRepository.findById(profileId).orElse(null);
//        if(profile == null) {
//            throw new IllegalArgumentException("Profile not found");
//        }
//        return modelMapper.map(profile, ProfileDTO.class);
//    }
}
