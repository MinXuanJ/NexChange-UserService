package com.nus.nexchange.userservice.api.dto;

import com.nus.nexchange.userservice.domain.entity.PostStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class WishPostDTO {
    private UUID postId;

    private UUID refPostId;

    private String refPostTitle;

    private PostStatus refPostStatus;

    private UUID wishPostListId;
}
