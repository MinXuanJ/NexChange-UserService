package com.nus.nexchange.userservice.api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProfileDTO {
    private UUID userProfileId;

    private UUID userId;

    private String userAvatarURL;

    private String userNickName;
}
