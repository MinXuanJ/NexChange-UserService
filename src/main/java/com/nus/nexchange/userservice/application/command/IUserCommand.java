package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;

public interface IUserCommand {
    public void createUser(UserIdentity userIdentity);
}
