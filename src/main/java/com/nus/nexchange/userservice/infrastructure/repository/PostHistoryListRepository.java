package com.nus.nexchange.userservice.infrastructure.repository;

import com.nus.nexchange.userservice.domain.aggregate.UserPostHistoryList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostHistoryListRepository extends JpaRepository<UserPostHistoryList, UUID> {
    UserPostHistoryList findByUserId(UUID userId);
}
