package com.nus.nexchange.userservice.application.service;

import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    public List<UserIdentity> getAllUsers();
    public UserIdentity createUser(UserIdentity userIdentity);
    public UserIdentity getUserById(UUID id);
}
