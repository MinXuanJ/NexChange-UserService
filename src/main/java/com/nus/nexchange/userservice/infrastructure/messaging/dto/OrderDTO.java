package com.nus.nexchange.userservice.infrastructure.messaging.dto;

import com.nus.nexchange.userservice.domain.entity.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderDTO {
    private UUID orderId;

    private String refPostTitle;

    private String refPostShortcutURL;

    private BigDecimal refPostPrice;

    private OrderStatus orderStatus;

    private UUID userId;
}
