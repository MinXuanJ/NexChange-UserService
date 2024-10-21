package com.nus.nexchange.userservice.domain.entity;

import com.nus.nexchange.userservice.domain.aggregate.UserOrderHistoryList;
import com.nus.nexchange.userservice.domain.aggregate.UserProfile;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class UserOrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID orderHistoryId;

    private UUID refOrderId;

    private String refOrderTitle;

    private String refOrderShoutCutURL;

    private String refOrderAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus refOrderStatus;

    @ManyToOne
    @JoinColumn(name = "order_history_list_id", nullable = false)
    private UserOrderHistoryList userOrderHistoryList;

    public UserOrderHistory(UUID refOrderId, String refOrderTitle, String refOrderShoutCutURL, String refOrderAmount, OrderStatus refOrderStatus, String refOrderURL) {
        this.refOrderId = refOrderId;
        this.refOrderTitle = refOrderTitle;
        this.refOrderShoutCutURL = refOrderShoutCutURL;
        this.refOrderAmount = refOrderAmount;
        this.refOrderStatus = refOrderStatus;
    }
}
