package com.nus.nexchange.userservice.application.service;

import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;
import com.nus.nexchange.userservice.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserIdentity> getAllUsers() {
        List<UserIdentity> entities = userRepository.findAll();
        return entities;
    }

    @Override
    public UserIdentity createUser(UserIdentity userIdentity) {
        UserIdentity savedEntity = userRepository.save(userIdentity);
        return savedEntity;
    }

    @Override
    public UserIdentity getUserById(UUID id){
        UserIdentity entity = userRepository.findById(id).orElse(null);
        return entity;
    }
}
