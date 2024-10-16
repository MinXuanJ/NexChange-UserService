package com.nus.nexchange.userservice.application.command;

import java.util.UUID;

public interface IPostHistoryListCommand {
    public void removePostHistory(UUID postHistoryId,UUID postHistoryListId);
}
