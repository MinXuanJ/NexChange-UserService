package com.nus.nexchange.userservice.application.command;

import com.nus.nexchange.userservice.api.dto.OrderHistories.OrderHistoryDTO;
import com.nus.nexchange.userservice.domain.entity.OrderStatus;

import java.util.UUID;

public interface IOrderHistoryListCommand {
    void addOrderHistory(OrderHistoryDTO orderHistoryDTO);

//    void updateOrderHistory(OrderHistoryDTO orderHistoryDTO);

    void updateOrderHistoryStatus(UUID userId, UUID orderId, OrderStatus orderStatus);

    void removeOrderHistory(UUID orderHistoryId, UUID orderHistoryListId);
}
