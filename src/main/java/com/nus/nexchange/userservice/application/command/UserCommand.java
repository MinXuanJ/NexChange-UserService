package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;
import com.nus.nexchange.userservice.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCommand implements IUserCommand {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createUser(UserIdentity userIdentity) {
        userRepository.save(userIdentity);
    }
}
