package com.nus.nexchange.userservice.domain.event;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserIdentityCreatedEvent {
    private final UUID userId;

    public UserIdentityCreatedEvent(UUID userId) {
        this.userId = userId;
    }

}
