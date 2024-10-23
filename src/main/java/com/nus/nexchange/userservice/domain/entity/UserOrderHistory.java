package com.nus.nexchange.userservice.domain.entity;

import com.nus.nexchange.userservice.domain.aggregate.UserOrderHistoryList;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("JpaDataSourceORMInspection")
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

    private BigDecimal refOrderPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus refOrderStatus;

    @ManyToOne
    @JoinColumn(name = "order_history_list_id", nullable = false)
    private UserOrderHistoryList userOrderHistoryList;
}
