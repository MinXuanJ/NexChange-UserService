package com.nus.nexchange.userservice.domain.services;

import com.nus.nexchange.userservice.domain.domainmodels.aggregates.UserIdentity;
import com.nus.nexchange.userservice.infrastructure.datamodels.UserIdentityEntity;
import com.nus.nexchange.userservice.infrastructure.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDomainService {

    private final UserRepository userRepository;

    @Autowired
    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // 获取所有用户
    public List<UserIdentity> getAllUsers() {
        List<UserIdentityEntity> entities = userRepository.findAll();
        return entities.stream()
                .map(this::mapToDomainModel)
                .collect(Collectors.toList());
    }
    // 创建用户
    public UserIdentity createUser(UserIdentity userIdentity) {
        UserIdentityEntity entity = mapToDataModel(userIdentity);
        UserIdentityEntity savedEntity = userRepository.save(entity);
        return mapToDomainModel(savedEntity);
    }

    // 根据 ID 获取用户
    public UserIdentity getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::mapToDomainModel)
                .orElse(null);
    }

    // 将 Data Model 映射到 Domain Model
    private UserIdentity mapToDomainModel(UserIdentityEntity entity) {
        return new UserIdentity(
                entity.getUserId(),
                entity.getUserName(),
                entity.getUserEmail(),
                entity.getUserPassword(),
                entity.getDateTimeCreated(),
                entity.getDateTimeUpdated()
        );
    }

    // 将 Domain Model 映射到 Data Model
    private UserIdentityEntity mapToDataModel(UserIdentity domain) {
        return new UserIdentityEntity(
                domain.getUserName(),
                domain.getUserEmail(),
                domain.getUserPassword()
        );
    }
}