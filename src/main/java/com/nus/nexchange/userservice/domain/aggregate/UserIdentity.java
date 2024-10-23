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
    @Column(nullable = false, unique = true, length = 16)
    private UUID userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String userPassword;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTimeCreated;

    @UpdateTimestamp
    @Column()
    private LocalDateTime dateTimeUpdated;
}
