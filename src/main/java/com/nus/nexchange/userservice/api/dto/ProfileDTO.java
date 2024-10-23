package com.nus.nexchange.userservice.api.dto;

import com.nus.nexchange.userservice.domain.entity.UserOrderHistory;
import com.nus.nexchange.userservice.domain.entity.UserPostHistory;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProfileDTO {
    private UUID userProfileId;

    private UUID userId;

    private String userAvatarURL;

    private String userNickName;
}
