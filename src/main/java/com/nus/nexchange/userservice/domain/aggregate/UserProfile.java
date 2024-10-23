package com.nus.nexchange.userservice.domain.aggregate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userProfileId;

    private UUID userId;

    private String userAvatarURL;

    private String userNickName;

    public UserProfile(UUID userId) {
        this.userId = userId;
    }
}
