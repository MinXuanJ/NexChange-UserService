package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.PostHistories.PostHistoryDTO;

import java.util.UUID;

public interface IPostHistoryListCommand {
    void addPostHistory(PostHistoryDTO postHistoryDTO);

    void updatePostHistory(PostHistoryDTO postHistoryDTO);

    //    public void removePostHistory(UUID postHistoryId,UUID postHistoryListId);
    void removePostHistory(UUID postId, UUID userId);
}
