package com.nus.nexchange.userservice.domain.aggregate;

import com.nus.nexchange.userservice.domain.model.UserWishpost;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class UserWishPostList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID wishPostListingId;

    private UUID userId;

    @OneToMany(mappedBy = "userWishPostList")
    private List<UserWishpost> wishPosts;

    public void addUserWishPost(UserWishpost userWishpost) {
        wishPosts.add(userWishpost);
        userWishpost.setUserWishPostList(this);
    }

    public void deleteUserWishPost(UserWishpost userWishpost) {
        wishPosts.remove(userWishpost);
        userWishpost.setUserWishPostList(null);
    }

//    public void displayAllUserWishPosts() {
//
//    }
}
