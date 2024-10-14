package com.nus.nexchange.userservice.api.dto.OrderHistories;

import com.nus.nexchange.userservice.domain.entity.OrderStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderHistoryDTO {
    private UUID orderHistoryListId;

    private UUID orderHistoryId;

    private UUID refOrderId;

    private String refOrderTitle;

    private String refOrderShoutCutURL;

    private String refOrderAmount;

    private OrderStatus refOrderStatus;
}