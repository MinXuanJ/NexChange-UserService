package com.nus.nexchange.userservice.api.dto.PostHistories;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PostHistoryListDTO {
    private UUID postHistoryListId;

    private UUID userId;

    private List<PostHistoryDTO> postHistories;
}
