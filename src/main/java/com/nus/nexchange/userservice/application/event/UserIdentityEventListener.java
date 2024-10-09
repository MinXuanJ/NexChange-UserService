package com.nus.nexchange.userservice.application.event;

import com.nus.nexchange.userservice.domain.aggregate.UserContactList;
import com.nus.nexchange.userservice.domain.aggregate.UserProfile;
import com.nus.nexchange.userservice.domain.event.UserIdentityCreatedEvent;
import com.nus.nexchange.userservice.infrastructure.repository.ContactListRepository;
import com.nus.nexchange.userservice.infrastructure.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserIdentityEventListener {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private ContactListRepository contactListRepository;

    @EventListener
    public void handleUserIdentityCreatedEvent(UserIdentityCreatedEvent event) {
        UUID userId = event.getUserId();

        UserProfile userProfile = new UserProfile(userId);
        UserContactList contactList = new UserContactList(userId);

        userProfileRepository.save(userProfile);
        contactListRepository.save(contactList);
    }
}
