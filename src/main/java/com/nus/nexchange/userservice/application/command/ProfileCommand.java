package com.nus.nexchange.userservice.application.command;


import com.nus.nexchange.userservice.api.dto.ProfileDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserProfile;
import com.nus.nexchange.userservice.infrastructure.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileCommand implements IProfileCommand {

    private final UserProfileRepository profileRepository;

    //    @Override
//    public void createProfile(ProfileDTO profile) {
//        profileRepository.save(profile);
//    }
    @Autowired
    public ProfileCommand(UserProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public void updateProfile(ProfileDTO profileDTO) {
        UserProfile profileFromDB = profileRepository.findByUserId(profileDTO.getUserId());

        if (profileFromDB == null) {
            throw new IllegalArgumentException("Profile does not exist");
        }
        profileFromDB.setUserAvatarURL(profileDTO.getUserAvatarURL());
        profileFromDB.setUserNickName(profileDTO.getUserNickName());

        profileRepository.save(profileFromDB);
    }

//    @Override
//    public void deleteProfile(UUID id) {
//        profileRepository.deleteById(id);
//    }
}
