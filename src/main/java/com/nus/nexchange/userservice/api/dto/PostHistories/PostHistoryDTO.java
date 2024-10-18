package com.nus.nexchange.userservice.api.dto.PostHistories;

import com.nus.nexchange.userservice.domain.entity.PostStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class PostHistoryDTO {
    private UUID postHistoryListId;

    private UUID userId;

    private UUID postHistoryId;

    private UUID refPostId;

    private String refPostTitle;

    private String refPostShortCutURL;

    private PostStatus refPostStatus;
}
