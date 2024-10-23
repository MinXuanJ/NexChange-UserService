package com.nus.nexchange.userservice.application.event;

import com.nus.nexchange.userservice.domain.aggregate.*;
import com.nus.nexchange.userservice.domain.event.UserIdentityCreatedEvent;
import com.nus.nexchange.userservice.infrastructure.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserIdentityEventListener {
    private final UserProfileRepository userProfileRepository;

    private final ContactListRepository contactListRepository;

    private final WishPostListRepository wishPostListRepository;

    private final OrderHistoryListRepository orderHistoryListRepository;

    private final PostHistoryListRepository postHistoryListRepository;

    @Autowired
    public UserIdentityEventListener(UserProfileRepository userProfileRepository, ContactListRepository contactListRepository, WishPostListRepository wishPostListRepository, OrderHistoryListRepository orderHistoryListRepository, PostHistoryListRepository postHistoryListRepository) {
        this.userProfileRepository = userProfileRepository;
        this.contactListRepository = contactListRepository;
        this.wishPostListRepository = wishPostListRepository;
        this.orderHistoryListRepository = orderHistoryListRepository;
        this.postHistoryListRepository = postHistoryListRepository;
    }

    @EventListener
    public void handleUserIdentityCreatedEvent(UserIdentityCreatedEvent event) {
        UUID userId = event.getUserId();

        UserProfile userProfile = new UserProfile(userId);
        UserContactList contactList = new UserContactList(userId);
        UserWishPostList wishPostList = new UserWishPostList(userId);
        UserOrderHistoryList orderHistoryList = new UserOrderHistoryList(userId);
        UserPostHistoryList postHistoryList = new UserPostHistoryList(userId);

        userProfileRepository.save(userProfile);
        contactListRepository.save(contactList);
        wishPostListRepository.save(wishPostList);
        orderHistoryListRepository.save(orderHistoryList);
        postHistoryListRepository.save(postHistoryList);
    }
}
