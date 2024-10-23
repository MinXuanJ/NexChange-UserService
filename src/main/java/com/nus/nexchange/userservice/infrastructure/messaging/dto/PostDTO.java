package com.nus.nexchange.userservice.infrastructure.messaging.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nus.nexchange.userservice.domain.entity.PostStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDTO {
    private UUID postId;

    private UUID userId;

    private String postTittle;

    private String postName;

    private String postDescription;

    private BigDecimal postPrice;

    private String postShortcutURL;

    private PostStatus postStatus;

    private LocalDateTime dateTimeCreated;

    private LocalDateTime dateTimeUpdated;
}
