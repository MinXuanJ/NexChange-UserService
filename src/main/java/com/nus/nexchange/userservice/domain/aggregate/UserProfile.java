package com.nus.nexchange.userservice.domain.aggregate;

import com.nus.nexchange.userservice.domain.model.UserOrderHistory;
import com.nus.nexchange.userservice.domain.model.UserPostHistory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userProfileId;

    private UUID userId;

    @OneToMany(mappedBy="userProfile")
    private List<UserPostHistory> userPostHistoryList;

    @OneToMany(mappedBy="userProfile")
    private List<UserOrderHistory> userOrderHistoryList;

//    public void displayPostHistory() {}
//
//    public void displayOrderHistory() {}
}
