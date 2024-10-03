package com.nus.nexchange.userservice.infrastructure.datamodels;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_identity") // The table in the database will be called "user_identity"
public class UserIdentityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // 自动生成的 UUID
    @Column(name = "user_id", nullable = false, unique = true, length = 36) // 明确指定长度为36
    private UUID userId;

    @Column(name = "user_name", nullable = false) // Column mapping with constraints
    private String userName;

    @Column(name = "user_email", nullable = false, unique = true) // Unique constraint on the email field
    private String userEmail;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime dateTimeCreated;

    @Column(name = "updated_at")
    private LocalDateTime dateTimeUpdated;

    // Default constructor required by JPA
    protected UserIdentityEntity() {
    }

    // Constructor for creating a new user entity
    public UserIdentityEntity(String userName, String userEmail, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.dateTimeCreated = LocalDateTime.now();
    }

    // Pre-persist hook for creation timestamp
    @PrePersist
    protected void onCreate() {
        dateTimeCreated = LocalDateTime.now();
    }

    // Pre-update hook for modification timestamp
    @PreUpdate
    protected void onUpdate() {
        dateTimeUpdated = LocalDateTime.now();
    }

    // Getters and Setters

    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public LocalDateTime getDateTimeCreated() {
        return dateTimeCreated;
    }

    public LocalDateTime getDateTimeUpdated() {
        return dateTimeUpdated;
    }
}
