package com.nus.nexchange.userservice.api.dto;

import com.nus.nexchange.userservice.domain.entity.UserWishPost;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class WishPostListDTO {
    private UUID wishPostListId;

    private UUID userId;

    private List<WishPostDTO> wishPosts;
}
