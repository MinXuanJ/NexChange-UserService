package com.nus.nexchange.userservice.domain.aggregate;

import com.nus.nexchange.userservice.domain.entity.OrderStatus;
import com.nus.nexchange.userservice.domain.entity.UserOrderHistory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class UserOrderHistoryList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderHistoryListId;

    private UUID userId;

    @OneToMany(mappedBy = "userOrderHistoryList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserOrderHistory> userOrderHistories;

    public UserOrderHistoryList(UUID userId) {
        this.userId = userId;
        userOrderHistories = new ArrayList<>();
    }

    public void addUserOrderHistory(UserOrderHistory userOrderHistory) {
        userOrderHistories.add(userOrderHistory);
        userOrderHistory.setUserOrderHistoryList(this);
    }

//    public void updateUserOrderHistory(UserOrderHistory userOrderHistory) {
//        if (userOrderHistory == null || userOrderHistory.getOrderHistoryId() == null) {
//            throw new IllegalArgumentException("userOrderHistory is null");
//        }
//
//        UserOrderHistory existingOrderHistory = userOrderHistories.stream()
//                .filter(userOrderHistoryDB -> userOrderHistoryDB.getOrderHistoryId().equals(userOrderHistory.getOrderHistoryId()))
//                .findFirst().orElse(null);
//
//        existingOrderHistory.setRefOrderStatus(userOrderHistory.getRefOrderStatus() != null ?
//                userOrderHistory.getRefOrderStatus() : existingOrderHistory.getRefOrderStatus());
//        existingOrderHistory.setRefOrderPrice(userOrderHistory.getRefOrderPrice() !=null ?
//                userOrderHistory.getRefOrderPrice() : existingOrderHistory.getRefOrderPrice());
//        existingOrderHistory.setRefOrderTitle(userOrderHistory.getRefOrderTitle() !=null ?
//                userOrderHistory.getRefOrderTitle() : existingOrderHistory.getRefOrderTitle());
//        existingOrderHistory.setRefOrderShoutCutURL(userOrderHistory.getRefOrderShoutCutURL() !=null ?
//                userOrderHistory.getRefOrderShoutCutURL() : existingOrderHistory.getRefOrderShoutCutURL());
//    }

    public void updateUserOrderHistoryStatus(UUID orderId, OrderStatus orderStatus) {

        UserOrderHistory existingOrderHistory = userOrderHistories.stream()
                .filter(userOrderHistoryDB -> userOrderHistoryDB.getRefOrderId().equals(orderId))
                .findFirst().orElse(null);

        Objects.requireNonNull(existingOrderHistory).setRefOrderStatus(orderStatus);
    }

    public void deleteOrderHistory(UUID orderHistoryId) {
        UserOrderHistory orderHistory = userOrderHistories.stream()
                .filter(userOrderHistory -> userOrderHistory.getOrderHistoryId().equals(orderHistoryId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("OrderHistory not found"));

        userOrderHistories.remove(orderHistory);
        orderHistory.setUserOrderHistoryList(null);
    }
}
