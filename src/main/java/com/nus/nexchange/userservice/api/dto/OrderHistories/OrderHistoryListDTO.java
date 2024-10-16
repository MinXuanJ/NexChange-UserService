package com.nus.nexchange.userservice.api.dto.OrderHistories;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderHistoryListDTO {
    private UUID orderHistoryListId;

    private UUID userId;

    private List<OrderHistoryDTO> userOrderHistories;
}
