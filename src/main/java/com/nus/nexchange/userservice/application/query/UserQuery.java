package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;
import com.nus.nexchange.userservice.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserQuery implements IUserQuery {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserIdentity> getAllUsers() {
        List<UserIdentity> entities = userRepository.findAll();
        return entities;
    }

    @Override
    public UserIdentity getUserById(UUID id) {
        UserIdentity entity = userRepository.findById(id).orElse(null);
        return entity;
    }

    @Override
    public boolean validateUser(UserIdentity userIdentity) {
        UserIdentity userFromDatabase = getUserById(userIdentity.getUserId());
        if (userFromDatabase != null) {
            return userFromDatabase.getUserEmail().equals(userIdentity.getUserEmail())
                    && userFromDatabase.getUserPassword().equals(userIdentity.getUserPassword());
        }

        return false;
    }
}
