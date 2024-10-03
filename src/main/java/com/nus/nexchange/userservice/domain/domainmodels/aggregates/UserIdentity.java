package com.nus.nexchange.userservice.domain.domainmodels.aggregates;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserIdentity {

    private UUID userId;
    private LocalDateTime datetimeCreated;
    private LocalDateTime datetimeUpdated;
    private String userName;
    private String userEmail;
    private String userPassword;

    public UserIdentity(UUID userId, String userName, String userEmail, String userPassword, LocalDateTime dateTimeCreated, LocalDateTime dateTimeUpdated) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.datetimeCreated = LocalDateTime.now();
    }

    // 业务逻辑：更新用户邮箱
    public boolean updateUserEmail(String newEmail) {
        if (newEmail != null && !newEmail.isEmpty()) {
            this.userEmail = newEmail;
            this.datetimeUpdated = LocalDateTime.now();
            return true;
        }
        return false;
    }

    // 业务逻辑：重置用户密码
    public boolean resetPassword(String newPassword) {
        if (newPassword != null && !newPassword.isEmpty()) {
            this.userPassword = newPassword;
            this.datetimeUpdated = LocalDateTime.now();
            return true;
        }
        return false;
    }

    // Getters
    public UUID getUserId() {
        return userId;
    }

    public LocalDateTime getDatetimeCreated() {
        return datetimeCreated;
    }

    public LocalDateTime getDatetimeUpdated() {
        return datetimeUpdated;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
