package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.PostHistories.PostHistoryDTO;

import java.util.UUID;

public interface IPostHistoryListCommand {
    public void addPostHistory(PostHistoryDTO postHistoryDTO);
    public void updatePostHistory(PostHistoryDTO postHistoryDTO);
    public void removePostHistory(UUID postHistoryId,UUID postHistoryListId);
}
