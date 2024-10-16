package com.nus.nexchange.userservice.infrastructure.repository;

import com.nus.nexchange.userservice.domain.aggregate.UserOrderHistoryList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderHistoryListRepository extends JpaRepository<UserOrderHistoryList, UUID> {
    public UserOrderHistoryList findByUserId(UUID userId);
}
