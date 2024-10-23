package com.nus.nexchange.userservice.domain.event;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserIdentityCreatedEvent {
    private final UUID userId;

    private final String userName;

    private final String avatarURL;

    public UserIdentityCreatedEvent(UUID userId, String userName, String avatarURL) {
        this.userId = userId;
        this.userName = userName;
        this.avatarURL = avatarURL;
    }

}
