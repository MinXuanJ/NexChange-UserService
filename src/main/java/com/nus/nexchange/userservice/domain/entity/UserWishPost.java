package com.nus.nexchange.userservice.domain.entity;

import com.nus.nexchange.userservice.domain.aggregate.UserWishPostList;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class UserWishPost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID wishPostId;

    private UUID refPostId;

    private String refPostTitle;

    @Enumerated(EnumType.STRING)
    private PostStatus refPostStatus;

    @ManyToOne
    @JoinColumn(name = "wishpost_list_id",nullable = false)
    private UserWishPostList userWishPostList;
}
