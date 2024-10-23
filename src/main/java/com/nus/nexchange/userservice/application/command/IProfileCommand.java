package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.ProfileDTO;


public interface IProfileCommand {
    //    public void createProfile(ProfileDTO profileDTO);
    void updateProfile(ProfileDTO profileDTO);
//    public void deleteProfile(UUID id);
}
