package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.ProfileDTO;

import java.util.UUID;

public interface IProfileQuery {
    ProfileDTO getUserProfileByUserId(UUID userId);
//    ProfileDTO getProfile(UUID profileId);
}
