package com.nus.nexchange.userservice.api.dto.Wishposts;

import com.nus.nexchange.userservice.domain.entity.PostStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WishPostDTO {
    private UUID wishPostId;

    private UUID refPostId;

    private String refPostTitle;

    private PostStatus refPostStatus;

    private UUID wishPostListId;

    private UUID userId;

    private BigDecimal refPostPrice;

    private String refPostShortCutURL;
}
