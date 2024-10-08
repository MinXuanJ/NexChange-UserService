package com.nus.nexchange.userservice.infrastructure.repository;

import com.nus.nexchange.userservice.domain.aggregate.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserIdentity, UUID> {
    // 自定义查询方法
    public UserIdentity findByUserName(String userName);

    // 其他自定义查询
    public UserIdentity findByUserEmail(String userEmail);

}
