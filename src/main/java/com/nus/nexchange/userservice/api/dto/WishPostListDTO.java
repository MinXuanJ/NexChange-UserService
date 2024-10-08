package com.nus.nexchange.userservice.api.dto;

import com.nus.nexchange.userservice.domain.entity.UserWishpost;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class WishPostListDTO {
    private UUID wishPostListingId;

    private UUID userId;

    private List<UserWishpost> wishPosts;
}
