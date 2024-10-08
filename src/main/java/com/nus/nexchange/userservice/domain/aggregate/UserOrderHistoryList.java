package com.nus.nexchange.userservice.domain.aggregate;

import com.nus.nexchange.userservice.domain.entity.UserOrderHistory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class UserOrderHistoryList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderHistoryListId;

    private UUID userId;

    @OneToMany(mappedBy = "userOrderHistoryList")
    private List<UserOrderHistory> userOrderHistories;
}
