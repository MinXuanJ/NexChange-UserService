package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.domain.aggregate.UserProfile;

import java.util.UUID;

public interface IProfileCommand {
    public void createProfile(UserProfile profile);
    public void updateProfile(UserProfile profile);
    public void deleteProfile(UUID id);
}
