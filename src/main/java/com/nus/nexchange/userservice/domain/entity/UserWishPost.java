package com.nus.nexchange.userservice.domain.entity;

import com.nus.nexchange.userservice.domain.aggregate.UserWishPostList;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("JpaDataSourceORMInspection")
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

    private BigDecimal refPostPrice;

    private String refPostShortCutURL;

    @ManyToOne
    @JoinColumn(name = "wishpost_list_id", nullable = false)
    private UserWishPostList userWishPostList;
}
