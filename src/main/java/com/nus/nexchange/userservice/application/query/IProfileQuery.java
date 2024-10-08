package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.domain.aggregate.UserProfile;

import java.util.UUID;

public interface IProfileQuery {
    public UserProfile getUserProfileByUserId(UUID userId);
}
