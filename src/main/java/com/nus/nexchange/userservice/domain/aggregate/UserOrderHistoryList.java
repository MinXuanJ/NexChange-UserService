package com.nus.nexchange.userservice.domain.aggregate;

import com.nus.nexchange.userservice.domain.entity.UserOrderHistory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "userOrderHistoryList",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserOrderHistory> userOrderHistories;

    public UserOrderHistoryList(UUID userId) {
        this.userId = userId;
        userOrderHistories = new ArrayList<>();
    }

    public void deleteOrderHistory(UUID orderHistoryId){
        UserOrderHistory orderHistory= userOrderHistories.stream()
                .filter(userOrderHistory -> userOrderHistory.getOrderHistoryId().equals(orderHistoryId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("OrderHistory not found"));

        userOrderHistories.remove(orderHistory);
        orderHistory.setUserOrderHistoryList(null);
    }
}
