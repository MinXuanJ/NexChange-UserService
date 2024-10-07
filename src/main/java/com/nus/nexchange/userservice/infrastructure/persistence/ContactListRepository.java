package com.nus.nexchange.userservice.infrastructure.persistence;

import com.nus.nexchange.userservice.domain.aggregate.UserContactList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactListRepository extends JpaRepository<UserContactList,UUID> {

}