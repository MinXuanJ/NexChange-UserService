package com.nus.nexchange.userservice.domain.model;

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

    private OrderStatus refOrderStatus;

    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    public UserOrderHistory(UUID refOrderId, String refOrderTitle, String refOrderShoutCutURL, String refOrderAmount, OrderStatus refOrderStatus, String refOrderURL) {
        this.refOrderId = refOrderId;
        this.refOrderTitle = refOrderTitle;
        this.refOrderShoutCutURL = refOrderShoutCutURL;
        this.refOrderAmount = refOrderAmount;
        this.refOrderStatus = refOrderStatus;
    }

    //    public UserOrderHistory displayOrderHistoryDetail(){
//
//    }
}
