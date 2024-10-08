package com.nus.nexchange.userservice.domain.aggregate;

import com.nus.nexchange.userservice.domain.entity.UserPostHistory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class UserPostHistoryList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID postHistoryListingId;

    private UUID userId;

    @OneToMany(mappedBy="userPostHistoryList")
    private List<UserPostHistory> userPostHistories;
}
