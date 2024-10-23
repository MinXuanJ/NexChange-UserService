package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.OrderHistories.OrderHistoryDTO;
import com.nus.nexchange.userservice.domain.entity.OrderStatus;

import java.util.UUID;

public interface IOrderHistoryListCommand {
    public void addOrderHistory(OrderHistoryDTO orderHistoryDTO);
    public void updateOrderHistory(OrderHistoryDTO orderHistoryDTO);
    public void updateOrderHistoryStatus(UUID userId, UUID orderId, OrderStatus orderStatus);
    public void removeOrderHistory(UUID orderHistoryId,UUID orderHistoryListId);
}
