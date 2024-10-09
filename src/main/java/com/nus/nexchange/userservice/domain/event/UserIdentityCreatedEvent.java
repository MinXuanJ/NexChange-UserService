package com.nus.nexchange.userservice.domain.event;

import java.util.UUID;

public class UserIdentityCreatedEvent {
    private final UUID userId;

    public UserIdentityCreatedEvent(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
