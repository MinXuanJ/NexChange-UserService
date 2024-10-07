package com.nus.nexchange.userservice.domain.aggregate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class UserIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false, unique = true, length = 36)
    private UUID userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime dateTimeCreated;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime dateTimeUpdated;

    public UserIdentity(String userName, String userEmail, String userPassword) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public boolean updateUserEmail(String newEmail) {
        if (newEmail != null && newEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            this.userEmail = newEmail;
            this.dateTimeUpdated = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public boolean resetPassword(String newPassword) {
        if (newPassword != null && newPassword.length() >= 8) {
            this.userPassword = newPassword;
            this.dateTimeUpdated = LocalDateTime.now();
            return true;
        }
        return false;
    }
}
