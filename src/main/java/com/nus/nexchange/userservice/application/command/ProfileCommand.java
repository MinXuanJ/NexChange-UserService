package com.nus.nexchange.userservice.application.command;


import com.nus.nexchange.userservice.api.dto.ProfileDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserProfile;
import com.nus.nexchange.userservice.infrastructure.repository.UserProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileCommand implements IProfileCommand {

    @Autowired
    private UserProfileRepository profileRepository;
    @Autowired
    private ModelMapper modelMapper;

//    @Override
//    public void createProfile(ProfileDTO profile) {
//        profileRepository.save(profile);
//    }

    @Override
    public void updateProfile(ProfileDTO profileFromDB, ProfileDTO profileDTO) {
        profileFromDB.setUserAvatarURL(profileDTO.getUserAvatarURL());
        profileFromDB.setUserNickName(profileDTO.getUserNickName());

        UserProfile profile = modelMapper.map(profileFromDB, UserProfile.class);
        profileRepository.save(profile);
    }

//    @Override
//    public void deleteProfile(UUID id) {
//        profileRepository.deleteById(id);
//    }
}
