package com.nus.nexchange.userservice.domain.aggregate;

import com.nus.nexchange.userservice.domain.entity.UserWishPost;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class UserWishPostList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID wishPostListId;

    private UUID userId;

    @OneToMany(mappedBy = "userWishPostList")
    private List<UserWishPost> wishPosts;

    public UserWishPostList(UUID userId) {
        this.userId = userId;
        wishPosts = new ArrayList<>();
    }

    public void addUserWishPost(UserWishPost userWishpost) {
        wishPosts.add(userWishpost);
        userWishpost.setUserWishPostList(this);
    }

    public void updateUserWishPost(UserWishPost userWishpost) {
        if (userWishpost == null || userWishpost.getPostId() == null){
            throw new IllegalArgumentException("userWishpost is null");
        }

        UserWishPost wishPostExist = wishPosts.stream()
                .filter(wishPost -> wishPost.getPostId().equals(userWishpost.getPostId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("userWishpost not found"));

        wishPostExist.setRefPostId(userWishpost.getRefPostId());
        wishPostExist.setRefPostTitle(userWishpost.getRefPostTitle());
        wishPostExist.setRefPostStatus(userWishpost.getRefPostStatus());
    }

    public void deleteUserWishPost(UUID wishPostId) {
        UserWishPost userWishPostToRemove = wishPosts.stream()
                .filter(wishPost -> wishPost.getPostId().equals(wishPostId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Wishpsot not found"));

        wishPosts.remove(userWishPostToRemove);
        userWishPostToRemove.setUserWishPostList(null);
    }
}
