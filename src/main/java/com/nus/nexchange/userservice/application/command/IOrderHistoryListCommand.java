package com.nus.nexchange.userservice.application.command;

import java.util.UUID;

public interface IOrderHistoryListCommand {
    public void removeOrderHistory(UUID orderHistoryId,UUID orderHistoryListId);
}
