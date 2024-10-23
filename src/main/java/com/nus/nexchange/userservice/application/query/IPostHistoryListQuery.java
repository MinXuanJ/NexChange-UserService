package com.nus.nexchange.userservice.application.query;

import com.nus.nexchange.userservice.api.dto.PostHistories.PostHistoryListDTO;

import java.util.UUID;

public interface IPostHistoryListQuery {
    PostHistoryListDTO getPostHistoryListByUserId(UUID postId);
}
