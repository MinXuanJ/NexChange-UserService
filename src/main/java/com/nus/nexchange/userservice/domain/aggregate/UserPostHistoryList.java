package com.nus.nexchange.userservice.domain.aggregate;

import com.nus.nexchange.userservice.domain.entity.UserPostHistory;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class UserPostHistoryList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID postHistoryListId;

    private UUID userId;

    @OneToMany(mappedBy="userPostHistoryList",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserPostHistory> userPostHistories;

    public UserPostHistoryList(UUID userId) {
        this.userId = userId;
        userPostHistories = new ArrayList<>();
    }

    public void addPostHistory(UserPostHistory userPostHistory) {
        userPostHistories.add(userPostHistory);
        userPostHistory.setUserPostHistoryList(this);
    }

    public void updatePostHistory(UserPostHistory userPostHistory) {
        if(userPostHistory == null){
            throw new IllegalArgumentException("userPostHistory is null");
        }

        UserPostHistory existingUserPostHistory = userPostHistories.stream()
                .filter(userPostHistoryDB->userPostHistoryDB.getRefPostId().equals(userPostHistory.getRefPostId()))
                .findFirst().orElse(null);

        existingUserPostHistory.setRefPostTitle(userPostHistory.getRefPostTitle());
        existingUserPostHistory.setRefPostStatus(userPostHistory.getRefPostStatus());
        existingUserPostHistory.setRefPostShortCutURL(userPostHistory.getRefPostShortCutURL());
    }

    public void deletePostHistory(UUID postHistoryId){
        UserPostHistory postHistory = userPostHistories.stream()
                .filter(userPostHistory -> userPostHistory.getPostHistoryId().equals(postHistoryId))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("UserPostHistory not found"));

        userPostHistories.remove(postHistory);
        postHistory.setUserPostHistoryList(null);
    }
}
