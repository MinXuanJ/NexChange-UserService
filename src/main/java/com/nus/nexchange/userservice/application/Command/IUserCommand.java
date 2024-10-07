package com.nus.nexchange.userservice.application.Command;

import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;

public interface IUserCommand {
    public void createUser(UserIdentity userIdentity);
}
