package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.ProfileDTO;

import java.util.UUID;

public interface IProfileQuery {
    public ProfileDTO getUserProfileByUserId(UUID userId);
    public ProfileDTO getProfile(UUID profileId);
}
