package com.nus.nexchange.userservice.infrastructure.repo;

import com.nus.nexchange.userservice.infrastructure.datamodels.UserIdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserIdentityEntity, UUID> {
    // 自定义查询方法
    UserIdentityEntity findByUserName(String userName);

    // 其他自定义查询
    UserIdentityEntity findByUserEmail(String userEmail);

}
