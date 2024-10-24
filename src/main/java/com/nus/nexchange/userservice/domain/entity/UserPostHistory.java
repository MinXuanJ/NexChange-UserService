package com.nus.nexchange.userservice.domain.entity;

import com.nus.nexchange.userservice.domain.aggregate.UserPostHistoryList;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Data
@NoArgsConstructor
public class UserPostHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID postHistoryId;

    private UUID refPostId;

    private String refPostTitle;

    private String refPostShortCutURL;

    private BigDecimal refPostPrice;

    @Enumerated(EnumType.STRING)
    private PostStatus refPostStatus;

    @ManyToOne
    @JoinColumn(name = "post_history_list_id", nullable = false)
    private UserPostHistoryList userPostHistoryList;
}
