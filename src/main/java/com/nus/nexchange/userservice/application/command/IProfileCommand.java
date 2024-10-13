package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.ProfileDTO;
import com.nus.nexchange.userservice.domain.aggregate.UserProfile;

import java.util.UUID;

public interface IProfileCommand {
    //    public void createProfile(ProfileDTO profileDTO);
    public void updateProfile(ProfileDTO profileFromDB, ProfileDTO profileDTO);
//    public void deleteProfile(UUID id);
}
