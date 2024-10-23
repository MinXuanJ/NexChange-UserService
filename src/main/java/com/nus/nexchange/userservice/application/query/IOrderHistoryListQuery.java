package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.OrderHistories.OrderHistoryListDTO;

import java.util.UUID;

public interface IOrderHistoryListQuery {
    OrderHistoryListDTO getOrderHistoryListByUserId(UUID userId);
}
