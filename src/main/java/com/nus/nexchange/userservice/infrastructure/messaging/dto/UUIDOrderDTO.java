package com.nus.nexchange.userservice.infrastructure.messaging.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UUIDOrderDTO {
    private UUID orderId;
    private UUID postId;
    private UUID userId;
    private UUID secret;
}
