package com.nus.nexchange.userservice.infrastructure.repository;

import com.nus.nexchange.userservice.domain.aggregate.UserWishPostList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WishPostListRepository extends JpaRepository<UserWishPostList, UUID> {
    public UserWishPostList findByUserId(UUID userId);
}
