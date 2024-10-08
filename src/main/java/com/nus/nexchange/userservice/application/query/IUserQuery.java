package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;

import java.util.List;
import java.util.UUID;

public interface IUserQuery {
    public List<UserIdentity> getAllUsers();
    public UserIdentity getUserById(UUID id);
}
